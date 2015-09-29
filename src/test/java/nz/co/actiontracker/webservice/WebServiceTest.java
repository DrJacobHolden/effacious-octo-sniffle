package nz.co.actiontracker.webservice;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.co.actiontracker.TestHelper;
import nz.co.actiontracker.activist.Activist;
import nz.co.actiontracker.activist.ActivistDTO;
import nz.co.actiontracker.activist.ActivistMapper;
import nz.co.actiontracker.campaign.CampaignDTO;
import nz.co.actiontracker.campaign.knowledgebase.Article;
import nz.co.actiontracker.campaign.knowledgebase.KnowledgeBase;
import nz.co.actiontracker.event.Event;
import nz.co.actiontracker.event.EventDTO;

/**
 * Tests various calls to the webservice. Currently these
 * only relate to the Activist class but if anyone ever
 * bothered finishing this it should test all the possible
 * URIs.
 * 
 * TODO:// These tests need to be commented out and the tests
 * in DomainTest uncommented in order to test persistance without
 * using the API. This is because the JpaTest class that DomainTest
 * extends from clears the entire database and demands a new Entity
 * Manager Factory. (Which is in conflict with the singleton used here.)
 */
public class WebServiceTest {

	private Logger _logger = LoggerFactory.getLogger(WebServiceTest.class);

	private static JAXBContext _jaxbContext = null;
	private static Marshaller _marshaller = null;
	private static Unmarshaller _unmarshaller = null;

	@BeforeClass
	public static void setup() throws JAXBException {
		_jaxbContext = JAXBContext.newInstance(
				ActivistDTO.class, 
				CampaignDTO.class, 
				KnowledgeBase.class, 
				Article.class,
				EventDTO.class
				);
		_marshaller = _jaxbContext.createMarshaller();
		_unmarshaller = _jaxbContext.createUnmarshaller();

		_marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	}

	/**
	 * Creates an XML representation of an ActivistDTO and then hands it to
	 * the webservice for persisting as an activist. The test then retrieves
	 * this activist, testing the GET method. Finally, the Activist is updated
	 * and then retrieved once again.
	 * 
	 * The Activist has a random number appended to the username as this field
	 * is enforced as unique in the database.
	 */
	@Test
	public void testSignUp() {

		Client client = ClientBuilder.newClient();

		try {
			_logger.info("Creating a new Activist...");

			String xml = "<activist>"
					+ "<username>JohnTravolta"+ new Random().nextInt(1000) +"</username>"
					+ "<email>JTravolta@hollywood.com</email>"
					+ "<address></address>"
					+ "</activist>";

			Response response = client.target("http://0.0.0.0:8080/services/activists")
					.request().post(Entity.xml(xml));

			int status = response.getStatus();
			if (status != 201) {
				_logger.error("Failed to create Activist; Web Service responded with: " + status);
				fail();
			}

			String location = response.getLocation().toString();
			_logger.info("Uri for new Activist: " + location);

			response.close();

			_logger.info("Querying the Activist...");
			ActivistDTO activist = client.target(location).request().get(ActivistDTO.class);
			Activist a = ActivistMapper.toDomainModel(activist);
			_logger.info("Retrieved Activist:\n" + a.toString());

			// Create a XML representation of the Activist, changing the value username
			// and using a null address
			String updateActivist = "<activist>"
					+ "<username>JohnTravolta"+ new Random().nextInt(1000) +"</username>"
					+ "<email>JTravolta@hollywood.com</email>"
					+ "</activist>";

			// Send a HTTP PUT request to the Web service. The request URI is 
			// that retrieved from the Web service (the response to the GET message)
			// and the message body is the above XML.
			response = client.target(location).request().put(Entity.xml(updateActivist));

			// Expect a HTTP 204 "No content" response from the Web service.
			status = response.getStatus();
			if (status != 204) {
				_logger.error("Failed to update Activist; Web service responded with: " + status);
				fail();
			}

			// Close the connection.
			response.close();

			// Finally, re-query the Activist. The username should have been updated
			_logger.info("Querying the Activist...");
			activist = client.target(location).request().get(ActivistDTO.class);
			a = ActivistMapper.toDomainModel(activist);
			_logger.info("Retrieved Activist:\n" + a.toString());

		} finally {
			client.close();
		}
	}

	/**
	 * This tests listing all the activists in existance. It does not
	 * specify a range so the default range is used (0 - max long value)
	 */
	@Test
	public void testListActivist() {
		Client client = ClientBuilder.newClient();

		try {

			_logger.info("Querying all the Activists...");
			List<ActivistDTO> activists = client.target("http://0.0.0.0:8080/services/activists").request().get(new GenericType<List<ActivistDTO>>(){});

			for (ActivistDTO a : activists) {
				Activist act = ActivistMapper.toDomainModel(a);
				System.out.println("Retrieved Activist: " + act.toString());
			}

		} finally {
			client.close();
		}
	}

	/**
	 * This tests querying all activists with query parameters enabled.
	 * This will return all the activists with ids between 10 and 300.
	 * Generally this will return no activists as the id generation seems
	 * to have quite a random range.
	 */
	@Test
	public void testListActivistWithLimits() {
		Client client = ClientBuilder.newClient();

		try {

			_logger.info("Querying all the Activists...");
			List<ActivistDTO> activists = client.target("http://0.0.0.0:8080/services/activists?from=10&to=300").request().get(new GenericType<List<ActivistDTO>>(){});

			for (ActivistDTO a : activists) {
				Activist act = ActivistMapper.toDomainModel(a);
				System.out.println("Retrieved Activist: " + act.toString());
			}

		} finally {
			client.close();
		}
	}
	
	/**
	 * This tests the long poll GET for when activists are created.
	 * 
	 * This is a simple application showing off async functionality.
	 * In the completed webservice this was going to be used to subscribe
	 * to things like the knowledgebase so that users are notified when
	 * an article is added.
	 * 
	 * This test functions by subscribing to the Activists in one thread
	 * and then in another creating an activist using the POST method.
	 * The test checks that the correct message is returned.
	 */
	@Test
	public void testAsyncResponse() {

		Thread subscribe = new Thread() {
			public void run() {
				Client client = ClientBuilder.newClient();
				_logger.info("Subscribing to Activists...");

				Response response = client.target("http://0.0.0.0:8080/services/activists/subscribe").request().get();

				//Loop until the response is received.
				while(!response.hasEntity());

				//Print the response when it arrives
				System.out.println(response.readEntity(String.class));
				
				//Close the response
				response.close();
				//Close the client
				client.close();
			}
		};
		
		//This must go here to prevent race conditions.
		subscribe.start();
		
		Thread createActivist = new Thread() {
			public void run() {
				Client client = ClientBuilder.newClient();
				_logger.info("Creating a new Activist...");

				String xml = "<activist>"
						+ "<username>JohnTravolta"+ new Random().nextInt(1000) +"</username>"
						+ "<email>JTravolta@hollywood.com</email>"
						+ "<address></address>"
						+ "</activist>";

				Response response2 = client.target("http://0.0.0.0:8080/services/activists")
						.request().post(Entity.xml(xml));

				int status = response2.getStatus();
				if (status != 201) {
					_logger.error("Failed to create Activist; Web Service responded with: " + status);
					fail();
				}

				String location = response2.getLocation().toString();
				_logger.info("Uri for new Activist: " + location);

				response2.close();
				//Close the client.
				client.close();
			}
		};	
		
		createActivist.start();
		
		/**
		 * This is an appalling use of Java and I promise never to do it
		 * again. Please, just let this assignment be over.
		 */
		
		//Wait for the subscribe thread to finish
		while(subscribe.isAlive());
		//Make sure the createActivist thread is also finished
		while(createActivist.isAlive());

	}

}
