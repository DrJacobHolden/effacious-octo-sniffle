package nz.co.actiontracker.jaxb;

import java.io.IOException;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import nz.co.actiontracker.activist.Activist;
import nz.co.actiontracker.activist.ActivistDTO;
import nz.co.actiontracker.campaign.Campaign;
import nz.co.actiontracker.campaign.CampaignDTO;
import nz.co.actiontracker.campaign.knowledgebase.Article;
import nz.co.actiontracker.campaign.knowledgebase.KnowledgeBase;
import nz.co.actiontracker.event.Event;
import nz.co.actiontracker.event.EventDTO;
import nz.co.actiontracker.event.EventMapper;

public class JaxbTest {

	private static JAXBContext _jaxbContext = null;
	private static Marshaller _marshaller = null;
	private static Unmarshaller _unmarshaller = null;
	private static ObjectMapper mapper = new ObjectMapper();
	
	@BeforeClass
	public static void setup() throws JAXBException {
		_jaxbContext = JAXBContext.newInstance(
				ActivistDTO.class, 
				CampaignDTO.class, 
				KnowledgeBase.class, 
				Article.class,
				EventDTO.class
				);
		mapper.registerModule(new JaxbAnnotationModule());
		_marshaller = _jaxbContext.createMarshaller();
		_unmarshaller = _jaxbContext.createUnmarshaller();
		
		_marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	}
	
	/**
	 * Tests the marshalling of a simple activist DTO.
	 */
	@Test
	public void marshallActivist() throws JAXBException {
		ActivistDTO a = new ActivistDTO(
				"John Travolta", "j.travolta@hollywood.com", null);
		
		_marshaller.marshal(a, System.out);
	}
	
	/**
	 * Tests the marshalling of a simple activist DTO to json.
	 * Json will work with all classes that Xml works for in this
	 * project by using the mapper instead of the marshaller.
	 */
	@Test
	public void marshallActivistJson() throws JsonGenerationException, IOException {
		ActivistDTO a = new ActivistDTO(
				"John Travolta", "j.travolta@hollywood.com", null);
		
		mapper.writeValue(System.out, a);
	}
	
	/**
	 * Tests the marshalling of a simple campaign DTO.
	 */
	@Test
	public void marshallCampaign() throws JAXBException {
		ActivistDTO a = new ActivistDTO(
				"John Travolta", "j.travolta@hollywood.com", null);
		CampaignDTO c = new CampaignDTO(
				"Go to the Shops", a);
		
		_marshaller.marshal(c, System.out);
	}
	
	/**
	 * Tests the marshalling of a KB with a single article.
	 */
	@Test
	public void marshallKB() throws JAXBException {
		Campaign c = new Campaign(
				"Stop Children Starving");
		
		KnowledgeBase KB = c.getKB();
		KB.addArticle(
				"Google", "www.google.com", 0);
		
		_marshaller.marshal(KB, System.out);
	}
	
	/**
	 * Tests the marshalling of an event with a single attendee.
	 */
	@Test
	public void marshallEvent() throws JAXBException {
		Activist john = new Activist(
				"JTravolta", "j.travolta@hollywood.com");
		Campaign camp = new Campaign(
				"Build Ponytail Statue at Beehive");
		john.createCampaign(camp);
		
		Event e = new Event(
				"Sit-In at the Rest Home", new Date(), 
				"The Rest Home", john, camp);
		
		e.rsvp(john);
		
		EventDTO eDTO = EventMapper.toDTO(e);
		
		_marshaller.marshal(eDTO, System.out);
	}
}
