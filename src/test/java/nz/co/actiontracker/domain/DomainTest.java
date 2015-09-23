package nz.co.actiontracker.domain;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.co.actiontracker.activist.Activist;
import nz.co.actiontracker.campaign.Campaign;
import nz.co.actiontracker.campaign.knowledgebase.KnowledgeBase;
import nz.co.actiontracker.event.Event;

public class DomainTest extends JpaTest {

	private static Logger _logger = LoggerFactory.getLogger(DomainTest.class);

	/*
	 * Tests
	 */

	/**
	 * Tests that a simple activist can be created and persisted.
	 */
	//@Test
	public void persistActivist() {

		_logger.info("Beginning persistActivist test.");

		_entityManager.getTransaction().begin();

		Activist john = new Activist("JTravolta", "j.travolta@hollywood.com");

		_logger.info("The activist has been created.");

		_entityManager.persist(john);

		//Checks the activist was actually added to the table
		List<Activist> activists = _entityManager.createQuery("select activist from Activist activist").getResultList();
		if(activists.isEmpty())
			fail("There should be an activist.");
		for(Activist activist : activists) {
			_logger.info("Retrieved Activist: " + activist.getUsername());
		}

		_entityManager.getTransaction().commit();

		_logger.info("Completed persistActivist test.");
	}

	/**
	 * Tests that a simple campaign can be created and persisted.
	 */
	//@Test
	public void createCampaign() {
		_logger.info("Beginning createCampaign test.");

		_entityManager.getTransaction().begin();

		Campaign camp = setupCampaign();
		
		_entityManager.persist(camp.getCreator());
		_entityManager.persist(camp);

		_entityManager.getTransaction().commit();

		_logger.info("Finishing createCampaign test.");
	}

	/**
	 * Tests subscribing to a simple campaign.
	 */
	//@Test
	public void subscribeToCampaign() {
		_logger.info("Beginning subscribeToCampaign test.");

		_entityManager.getTransaction().begin();

		Campaign camp = setupCampaign();
		Activist jim = new Activist("JSterling", "j.sterling@youtube.com");

		_logger.info("The activists have been created.");
		
		assertTrue(jim.subscribeTo(camp));

		_logger.info(jim.toString() + " has subscribed to " + camp.toString());

		_entityManager.persist(camp.getCreator());
		_entityManager.persist(jim);
		_entityManager.persist(camp);

		_entityManager.getTransaction().commit();

		_logger.info("Finishing subscribeToCampaign test.");
	}

	/**
	 * Tests unsubscribing from a simple campaign.
	 */
	//@Test
	public void unsubscribeFromCampaign() {
		_logger.info("Beginning unsubscribeFromCampaign test.");

		_entityManager.getTransaction().begin();
		
		Campaign camp = setupCampaign();
		Activist jim = new Activist("JSterling", "j.sterling@youtube.com");

		_logger.info("The activists have been created.");
		
		assertTrue(jim.subscribeTo(camp));

		_logger.info(jim.toString() + " has subscribed to " + camp.toString());

		assertTrue(jim.unsubscribeFrom(camp));

		_logger.info(jim.toString() + " has unsubscribed from " + camp.toString());

		_entityManager.persist(camp.getCreator());
		_entityManager.persist(jim);
		_entityManager.persist(camp);

		_entityManager.getTransaction().commit();

		_logger.info("Finishing unsubscribeFromCampaign test.");
	}

	/**
	 * Tests adding an article to a KB.
	 */
	//@Test
	public void addArticleToKB() {
		_logger.info("Beginning addArticleToKB test.");

		_entityManager.getTransaction().begin();
		
		Campaign camp = setupCampaign();
		
		KnowledgeBase KB = camp.getKB();
				
		assertTrue(KB.addArticle("John Key, Prime Minister or Predator?", "www.google.com", camp.getCreator().getId()));
		
		_logger.info("Created article " + KB.getArticles().toArray()[0].toString() + " in the " + KB.toString() + " Knowledge Base.");
		
		_entityManager.persist(camp.getCreator());
		_entityManager.persist(camp);
		
		_entityManager.getTransaction().commit();

		_logger.info("Finishing addArticleToKB test.");
	}
	
	/**
	 * Tests removing an article from a KB.
	 */
	//@Test
	public void removeArticleFromKB() {
		_logger.info("Beginning removeArticleFromKB test.");

		_entityManager.getTransaction().begin();
		
		Campaign camp = setupCampaign();
		
		KnowledgeBase KB = camp.getKB();
				
		assertTrue(KB.addArticle("John Key, Prime Minister or Predator?", "www.google.com", camp.getCreator().getId()));
		
		_logger.info("Created article " + KB.getArticles().toArray()[0].toString() + " in the " + KB.toString() + " Knowledge Base.");
		
		assertTrue(KB.removeArticle("John Key, Prime Minister or Predator?", "www.google.com", camp.getCreator().getId()));
		
		_logger.info("Removed an article from the " + KB.toString() + " Knowledge Base.");
		
		_entityManager.persist(camp.getCreator());
		_entityManager.persist(camp);
		
		_entityManager.getTransaction().commit();

		_logger.info("Finishing addArticleToKB test.");
	}
	
	/**
	 * Tests creating an event.
	 */
	//@Test
	public void createEvent() {
		_logger.info("Beginning createEvent test.");

		_entityManager.getTransaction().begin();
		
		Event e = setupEvent();
		
		_entityManager.persist(e.getCreator());
		_entityManager.persist(e.getCampaign());
		_entityManager.persist(e);
		
		_entityManager.getTransaction().commit();

		_logger.info("Finishing createEvent test.");
	}
	
	/**
	 * Tests removing an event.
	 */
	//@Test
	public void removeEvent() {
		_logger.info("Beginning removeEvent test.");

		_entityManager.getTransaction().begin();
		
		Event e = setupEvent();
		
		Activist creator = e.getCreator();
		Campaign campaign = e.getCampaign();
		
		assertTrue(creator.cancelEvent(campaign, e));
		
		_logger.info("Successfully cancelled the event " + e.toString() + ".");
		
		_entityManager.persist(creator);
		_entityManager.persist(campaign);
		_entityManager.persist(e);
		
		_entityManager.getTransaction().commit();

		_logger.info("Finishing removeEvent test.");
	}
	
	/**
	 * Tests RSVPing to an event
	 */
	//@Test
	public void RSVPEvent() {
		_logger.info("Beginning RSVPEvent test.");

		_entityManager.getTransaction().begin();
		
		Event e = setupEvent();
		
		Activist jim = new Activist("JSterling", "j.sterling@youtube.com");
		
		assertTrue(jim.RSVPEvent(e));
		
		_logger.info(jim.toString() + " has RSVPed to " + e.toString());
		
		_entityManager.persist(e.getCreator());
		_entityManager.persist(jim);
		_entityManager.persist(e.getCampaign());
		_entityManager.persist(e);
		
		_entityManager.getTransaction().commit();

		_logger.info("Finishing RSVPEvent test.");
	}
	
	/**
	 * Tests ,unRSVPing to an event
	 */
	//@Test
	public void unRSVPEvent() {
		_logger.info("Beginning unRSVPEvent test.");

		_entityManager.getTransaction().begin();
		
		Event e = setupEvent();
		
		Activist jim = new Activist("JSterling", "j.sterling@youtube.com");
		
		assertTrue(jim.RSVPEvent(e));
		
		_logger.info(jim.toString() + " has RSVPed to " + e.toString());
		
		assertTrue(jim.unRSVPEvent(e));
		
		_logger.info(jim.toString() + " has unRSVPed to " + e.toString());
		
		_entityManager.persist(e.getCreator());
		_entityManager.persist(jim);
		_entityManager.persist(e.getCampaign());
		_entityManager.persist(e);
		
		_entityManager.getTransaction().commit();

		_logger.info("Finishing unRSVPEvent test.");
	}
	/*
	 * Helper functions
	 */
	
	/**
	 * Helper function that sets up a campaign and an activist.
	 * Returns the campaign. The owner can be retrieved by using
	 * camp.getOwner()
	 */
	public Campaign setupCampaign() {
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
	public Event setupEvent() {
		Campaign camp = setupCampaign();
		Activist creator = camp.getCreator();
		Event e = new Event("Sit-In at the Rest Home", new Date(), "The Rest Home");
		
		assertTrue(creator.createEvent(camp, e));
		
		_logger.info("The event " + e.toString() + " has been created.");
		return e;
	}
}