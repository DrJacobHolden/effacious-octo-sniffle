package nz.co.actiontracker.webservice;

import static org.junit.Assert.fail;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.co.actiontracker.activist.ActivistDTO;
import nz.co.actiontracker.campaign.CampaignDTO;
import nz.co.actiontracker.campaign.knowledgebase.Article;
import nz.co.actiontracker.campaign.knowledgebase.KnowledgeBase;
import nz.co.actiontracker.event.EventDTO;

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
	
	@Test
	public void testSignUp() {
		
		Client client = ClientBuilder.newClient();
		
		try {
			_logger.info("Creating a new Activist...");
			
			String xml = "<activist>"
					+ "<username>FrankieIsACutie</username>"
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
			
		} finally {
			client.close();
		}
	}
	
}
