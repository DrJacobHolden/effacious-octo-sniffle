package nz.co.actiontracker.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.BeforeClass;
import org.junit.Test;

import nz.co.actiontracker.activist.ActivistDTO;
import nz.co.actiontracker.campaign.CampaignDTO;

public class JaxbTest {

	private static JAXBContext _jaxbContext = null;
	private static Marshaller _marshaller = null;
	private static Unmarshaller _unmarshaller = null;
	
	@BeforeClass
	public static void setup() throws JAXBException {
		_jaxbContext = JAXBContext.newInstance(ActivistDTO.class, CampaignDTO.class);
		_marshaller = _jaxbContext.createMarshaller();
		_unmarshaller = _jaxbContext.createUnmarshaller();
		
		_marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	}
	
	@Test
	public void marshallActivist() throws JAXBException {
		ActivistDTO a = new ActivistDTO("John Travolta", "j.travolta@hollywood.com", null);
		
		_marshaller.marshal(a, System.out);
	}
	
	@Test
	public void marshallCampaign() throws JAXBException {
		ActivistDTO a = new ActivistDTO("John Travolta", "j.travolta@hollywood.com", null);
		CampaignDTO c = new CampaignDTO("Go to the Shops", a);
		
		_marshaller.marshal(c, System.out);
	}
}
