package nz.co.actiontracker.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to represent an Activist. This is created
 * by a  user if they choose to sign up and persists
 * in the database.
 * 
 * @author Tate Robertson
 */
@Entity
@Table(name="ACTIVISTS")
@XmlRootElement(name="activist")
public class Activist {
	
	private static Logger _logger = LoggerFactory.getLogger(Activist.class);
	
	/*
	 * Functions/Methods
	 */
	
	/**
	 * Subscribe to a campaign.
	 * 
	 * @param campaign
	 * 		The campaign to subscribe to.
	 */
	public void subscribeTo(Campaign campaign) {
		if(!_subscribed.contains(campaign)) {
			_subscribed.add(campaign);
			campaign.addSubscriber(this);
		} else {
			_logger.warn(this.toString() + "is already subscribed to " + campaign.toString());
		}
		
	}

	/**
	 * Unsubscribe from a campaign.
	 * 
	 * @param campaign
	 * 		The campaign to unsubscribe from.
	 */
	public void unsubscribeFrom(Campaign campaign) {
		if (_subscribed.contains(campaign)) {
			_subscribed.remove(campaign);
			campaign.removeSubscriber(this);
		} else {
			_logger.warn(this.toString() + " is not subscribed to " + campaign.toString());
		}
	}
	
	/**
	 * Makes this activist the owner of a campaign.
	 * 
	 * This method does not create the campaign object,
	 * instead it expects to be passed the campaign object.
	 */
	public void createCampaign(Campaign campaign) {
		created.add(campaign);
		campaign.setCreator(this);
	}
	
	@Override
	public String toString() {
		return _username;
	}
	
	/*
	 * Fields
	 */
	
	@Id
	@GeneratedValue(generator="ID_GENERATOR")
	private long _id;

	/**
	 * The username of this activist. This must be unique.
	 */
	@Column(unique=true, name="USERNAME", nullable=false)
	private String _username;

	/**
	 * The email of this activist.
	 */
	@Column(name="EMAIL", nullable=false)
	private String _email;

	/**
	 * The address of this activist.
	 */
	@Embedded
	private Address _address;

	/**
	 * The campaigns this activist has created.
	 */
	@OneToMany(
			targetEntity=Campaign.class
			)
	@JoinTable(
			name="CREATOR_TABLE",
			joinColumns=@JoinColumn(name="ACTIVIST_ID"),
			inverseJoinColumns=@JoinColumn(name="CAMPAIGN_ID")
			)
	private Set<Campaign> created = new HashSet<Campaign>();

	/**
	 * The set of campaigns an activist is subscribed to.
	 */
	@ManyToMany(
			targetEntity=Campaign.class,
			cascade={CascadeType.PERSIST, CascadeType.MERGE}
			)
	@JoinTable(
			name="SUBSCRIPTION_TABLE",
			joinColumns=@JoinColumn(name="ACTIVIST_ID"),
			inverseJoinColumns=@JoinColumn(name="CAMPAIGN_ID")
			)
	private Set<Campaign> _subscribed = new HashSet<Campaign>();

	/*
	 * Constructors
	 */
	
	public Activist(String username, String email, Address address) {
		this._username = username;
		this._email = email;
		this._address = address;
	}

	/**
	 * Create an activist with the minimum level of info
	 */
	public Activist(String username, String email) {
		this(username, email, null);
	}
	
	protected Activist() {}
	
	
	/*
	 * Setters and Getters
	 */

	public String getUsername() {
		return _username;
	}

	public long getId() {
		return _id;
	}
	
	public Collection<Campaign> getSubscribed() {
		return _subscribed;
	}
}
