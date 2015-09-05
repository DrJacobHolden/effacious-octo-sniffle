package nz.co.actiontracker.domain;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class to illustrate the behaviour of JPA in generating relational 
 * schemas and SQL, and with more generally managing object persistence.
 * 
 * This class inherits from JpaTest, which manages database connectivity, 
 * JPA initialisation and takes care of clearing out the database immediately
 * prior to executing each unit test. This ensures that there are no side-
 * effects of running any tests.
 * 
 * To see the effect of any particular test, you may want to comment out the 
 * @Test annotations on other tests. You can then use the H2 console to view 
 * the effect of the test of interest.
 * 
 * @author Ian Warren
 *
 */
public class DomainTest extends JpaTest {

	private static Logger _logger = LoggerFactory.getLogger(DomainTest.class);

	/*
	 * Basic table tests
	 */

	/**
	 * Tests that a simple activist can be created and persisted.
	 */
	@Test
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
	@Test
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
	@Test
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
	@Test
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
	@Test
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
	@Test
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
	 * Helper function that sets up a campaign and an activist.
	 * Returns the campaign.
	 */
	public Campaign setupCampaign() {
		Activist john = new Activist("JTravolta", "j.travolta@hollywood.com");
		
		Campaign camp = new Campaign("Build Ponytail Statue at Beehive");
		john.createCampaign(camp);

		_logger.info("The campaign " + camp.getName() + " has been created by " + camp.getCreator().toString() + ".");
		return camp;
	}
	/**
	@Test
	public void persistItemWithBids() {
		_entityManager.getTransaction().begin();

		Item dvd = new Item("American Sniper DVD");
		dvd.addImage(new Image("DVD", "image_675.png", 640, 480));
		dvd.addImage(new Image("DVD case", "image_676.png", 800, 600));
		dvd.addImage(new Image("DVD booklet", "image_678.png", 1024, 768));

		Item iPhone = new Item("iPhone");
		iPhone.addImage(new Image("DVD", "iPhone.jpg", 640, 480));

		Item speaker = new Item("Bluetooth speaker");

		_entityManager.persist(dvd);
		_entityManager.persist(iPhone);
		_entityManager.persist(speaker);

		Bid bidOne = new Bid(dvd, new BigDecimal(18.50));
		dvd.addBid(bidOne);

		Bid bidTwo = new Bid(dvd, new BigDecimal(20.00));
		dvd.addBid(bidTwo);

		Bid bidThree = new Bid(dvd, new BigDecimal(22.00));
		dvd.addBid(bidThree);

		Bid bidFour = new Bid(iPhone, new BigDecimal(350.00));
		iPhone.addBid(bidFour);

		_entityManager.getTransaction().commit();
	}

	@Test
	public void deleteItemWithBid() {
		_entityManager.getTransaction().begin();

		Item dvd = new Item("American Sniper DVD");
		dvd.addImage(new Image("DVD", "image_675.png", 640, 480));
		dvd.addImage(new Image("DVD case", "image_676.png", 800, 600));
		dvd.addImage(new Image("DVD booklet", "image_678.png", 1024, 768));

		Item iPhone = new Item("iPhone");
		iPhone.addImage(new Image("DVD", "iPhone.jpg", 640, 480));

		Item speaker = new Item("Bluetooth speaker");

		_entityManager.persist(dvd);
		_entityManager.persist(iPhone);
		_entityManager.persist(speaker);

		Bid bidOne = new Bid(dvd, new BigDecimal(18.50));
		dvd.addBid(bidOne);

		Bid bidTwo = new Bid(dvd, new BigDecimal(20.00));
		dvd.addBid(bidTwo);

		Bid bidThree = new Bid(dvd, new BigDecimal(22.00));
		dvd.addBid(bidThree);

		Bid bidFour = new Bid(iPhone, new BigDecimal(350.00));
		iPhone.addBid(bidFour);

		_entityManager.remove(iPhone);
		_entityManager.getTransaction().commit();
	}

	@Test
	public void queryBillingDetails() {
		_entityManager.getTransaction().begin();

		BillingDetails[] accounts = new BillingDetails[5];
		accounts[0] = new CreditCard("Amy", "4999...", "Apr-2015");
		accounts[1] = new CreditCard("Kim", "4999...", "Mar-2017");
		accounts[2] = new BankAccount("Pete", "50887471", "ANZ");
		accounts[3] = new BankAccount("John", "83846883", "ASB");
		accounts[4] = new CreditCard("Geoff", "4556...", "Dec-2016");

		for(int i = 0; i < accounts.length; i++) {
			_entityManager.persist(accounts[i]);
		}

		 List<BillingDetails> billingDetails = _entityManager.createQuery("select bd from BillingDetails bd").getResultList();
		 for(BillingDetails bd : billingDetails) {
			 _logger.info("Retrieved: " + bd.getClass().getName());
		 }

		 _entityManager.getTransaction().commit();
	}

	@Test
	public void persistUserWithBillingDetails() {
		_entityManager.getTransaction().begin();

		BillingDetails billing = new CreditCard("Amy", "4999...", "Apr-2015");
		User amy = new User("amy", "Johnson", "Amy");
		amy.setDefaultBillingDetails(billing);
		amy.setAddress(AddressType.SHIPPING, new Address("Sydney Gardens", "Auckland", "1010"));
		amy.setAddress(AddressType.BILLING, new Address("Sydney Gardens", "Auckland", "1010"));
		amy.setAddress(AddressType.HOME, new Address("Sydney Gardens", "Auckland", "1010"));

		User neil = new User("neil", "Armstrong", "Neil");
		neil.setDefaultBillingDetails(billing);
		neil.setAddress(AddressType.SHIPPING, new Address("Small Crater", "The Moon", "0000"));
		neil.setAddress(AddressType.BILLING, new Address("Small Crater", "The Moon", "0000"));
		neil.setAddress(AddressType.HOME, new Address("Small Crater", "The Moon", "0000"));


		User felix = new User("felix", "Baumgartner", "Felix");
		felix.setAddress(AddressType.SHIPPING, new Address("Balloon", "Edge of space", "0000"));
		felix.setAddress(AddressType.BILLING, new Address("Balloon", "Edge of space", "0000"));
		felix.setAddress(AddressType.HOME, new Address("Balloon", "Edge of space", "0000"));


		BillingDetails otherbilling = new CreditCard("Kim", "4999...", "Mar-2017");

		_entityManager.persist(billing);
		_entityManager.persist(amy);
		_entityManager.persist(neil);
		_entityManager.persist(felix);
		_entityManager.persist(otherbilling);

		List<User> users = _entityManager.createQuery("select user from User user").getResultList();
		 for(User user : users) {
			 BillingDetails bd = user.getDefaultBillingDetails();
			 if(bd != null) {
				 _logger.info("Retrieved User: " + user.getUserName() + " - " + bd.getClass().getName());
			 } else {
				 _logger.info("Retrieved User: " + user.getUserName() + " - no billing details");
			 }
		 }

		 _entityManager.getTransaction().commit();
	}

	@Test
	public void persistItemWithoutBuyer() {
		_entityManager.getTransaction().begin();

		Item dvd = new Item("American Sniper DVD");

		_entityManager.persist(dvd);

		_entityManager.getTransaction().commit();
	}

	@Test
	public void persistItemWithBuyer() {
		_entityManager.getTransaction().begin();

		Item dvd = new Item("American Sniper DVD");

		BillingDetails billing = new CreditCard("NASA", "4999...", "Apr-2015");

		User neil = new User("neil", "Armstrong", "Neil");

		neil.setDefaultBillingDetails(billing);
		neil.setAddress(AddressType.SHIPPING, new Address("Small Crater", "The Moon", "0000"));
		neil.setAddress(AddressType.BILLING, new Address("Small Crater", "The Moon", "0000"));
		neil.setAddress(AddressType.HOME, new Address("Small Crater", "The Moon", "0000"));

		dvd.setBuyer(neil);
		neil.addBoughtItem(dvd);

		// Note the ordering.
		_entityManager.persist(billing);
		_entityManager.persist(neil);
		_entityManager.persist(dvd);

		_entityManager.getTransaction().commit();
	}

	@Test
	public void persistCategoriesAndItems() {
		_entityManager.getTransaction().begin();

		Category sport = new Category("Sport");
		Category bicycles = new Category("Bicycles");

		Item triathleteBike = new Item("Litespeed Triathlete");
		Item kidsBike = new Item("Raleigh Chopper");

		sport.addItem(triathleteBike);
		bicycles.addItem(triathleteBike);
		bicycles.addItem(kidsBike);

		triathleteBike.addCategory(sport);
		triathleteBike.addCategory(bicycles);
		kidsBike.addCategory(bicycles);

		_entityManager.persist(sport);
		_entityManager.persist(bicycles);

		_entityManager.getTransaction().commit();
	}*/
}