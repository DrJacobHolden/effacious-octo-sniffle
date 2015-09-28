package nz.co.actiontracker;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.co.actiontracker.activist.Activist;
import nz.co.actiontracker.campaign.Campaign;
import nz.co.actiontracker.domain.DomainTest;
import nz.co.actiontracker.event.Event;

public class TestHelper {

	private static Logger _logger = LoggerFactory.getLogger(TestHelper.class);
	
	/**
	 * Helper function that sets up a campaign and an activist.
	 * Returns the campaign. The owner can be retrieved by using
	 * camp.getOwner()
	 */
	public static Campaign setupCampaign() {
		Activist john = new Activist("JTravolta", "j.travolta@hollywood.com");
		
		Campaign camp = new Campaign("Build Ponytail Statue at Beehive");
		john.createCampaign(camp);

		_logger.info("The campaign " + camp.getName() + " has been created by " + camp.getCreator().toString() + ".");
		return camp;
	}

	/**
	 * Helper function that sets up an event.
	 * 
	 * This returns the event.
	 */
	public static Event setupEvent() {
		Campaign camp = setupCampaign();
		Activist creator = camp.getCreator();
		Event e = new Event("Sit-In at the Rest Home", new Date(), "The Rest Home");
		
		assertTrue(creator.createEvent(camp, e));
		
		_logger.info("The event " + e.toString() + " has been created.");
		return e;
	}
	
}
