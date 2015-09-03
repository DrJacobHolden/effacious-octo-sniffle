package nz.co.actiontracker.domain;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.co.actiontracker.activist.domain.Activist;
import nz.co.actiontracker.campaign.domain.Campaign;

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


	@Test
	public void persistActivist() {
		_entityManager.getTransaction().begin();
		
		Activist john = new Activist("JTravolta", "j.travolta@hollywood.com");

		_logger.info("The activist has been created.");
		
		_entityManager.persist(john);

		_entityManager.getTransaction().commit();
	}
	
	@Test
	public void persistCampaign() {
		_entityManager.getTransaction().begin();
		
		Campaign camp = new Campaign("Recall American Sniper");
		
		_logger.info("The campaign " + camp.getName() + " has been created.");

		_entityManager.persist(camp);

		_entityManager.getTransaction().commit();
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