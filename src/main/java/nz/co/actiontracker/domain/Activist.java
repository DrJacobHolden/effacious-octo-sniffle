package nz.co.actiontracker.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
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
	 * Returns false if the activist is already subscribed to the campaign.
	 * 
	 * @param campaign
	 * 		The campaign to subscribe to.
	 */
	public boolean subscribeTo(Campaign campaign) {
		if(!_subscribed.contains(campaign) && campaign.addSubscriber(this)) {
			_subscribed.add(campaign);
			return true;
		} else {
			_logger.warn(this.toString() + "is already subscribed to " + campaign.toString());
			return false;
		}
	}

	/**
	 * Unsubscribe from a campaign.
	 * 
	 * Returns false if the activist is not subscribed to the campaign.
	 * 
	 * @param campaign
	 * 		The campaign to unsubscribe from.
	 */
	public boolean unsubscribeFrom(Campaign campaign) {
		if (_subscribed.contains(campaign) && campaign.removeSubscriber(this)) {
			_subscribed.remove(campaign);
			return true;
		} else {
			_logger.warn(this.toString() + " is not subscribed to " + campaign.toString());
			return false;
		}
	}

	/**
	 * RSVP to an event.
	 * 
	 * Returns false if you are already rsvped.
	 */
	public boolean RSVPEvent(Event e) {
		if(!_rsvped.contains(e) && e.rsvp(this)) {
			_rsvped.add(e);
			return true;
		} else {
			_logger.warn(this.toString() + "is already rsvped to " + e.toString());
			return false;
		}
	}
	
	/**
	 * unRSVP to an event.
	 * 
	 * Returns false if you are not rsvped.
	 */
	public boolean unRSVPEvent(Event e) {
		if(_rsvped.contains(e) && e.unrsvp(this)) {
			_rsvped.remove(e);
			return true;
		} else {
			_logger.warn(this.toString() + "is not rsvped to " + e.toString());
			return false;
		}
	}
	
	/**
	 * Makes this activist the owner of an event
	 * 
	 * This method does not create the event object,
	 * instead it expects to be passed the event object
	 * and the campaign object it is related to.
	 * 
	 * Returns false if the event already exists within the campaign.
	 */
	public boolean createEvent(Campaign campaign, Event e) {
		if(campaign.addEvent(e)) {
			_createdEvents.add(e);
			e.setCreator(this);
			return true;
		} else {
			_logger.error("Unable to add event " + e.toString()
					+ " it is already part of campaign " + campaign.toString());
			return false;
		}
	}

	/**
	 * Cancel an event.
	 * 
	 * Specifically, this method removes this activist as the owner,
	 * and disassociates the event from the specified campaign. This
	 * can only be called by the creator of the event.
	 */
	public boolean cancelEvent(Campaign campaign, Event e) {
		
		if (e.getCreator().getId() != this.getId()) {
			_logger.error("Unable to remove event " + e.toString()
			+ " as " + this.toString() + " is not the owner.");
			return false;
		}
		
		if(_createdEvents.contains(e) && campaign.removeEvent(e)) {
			_createdEvents.remove(e);
			e.setCreator(null);
			e.setCampaign(null);
			return true;
		} else {
			_logger.error("Unable to remove event " + e.toString()
					+ " it is not part of campaign " + campaign.toString());
			return false;
		}
	}
	
	/**
	 * Makes this activist the owner of a campaign.
	 * 
	 * This method does not create the campaign object,
	 * instead it expects to be passed the campaign object.
	 */
	public void createCampaign(Campaign campaign) {
		_createdCampaigns.add(campaign);
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
			name="CAMPAIGN_CREATOR_TABLE",
			joinColumns=@JoinColumn(name="ACTIVIST_ID"),
			inverseJoinColumns=@JoinColumn(name="CAMPAIGN_ID")
			)
	private Set<Campaign> _createdCampaigns = new HashSet<Campaign>();
	
	/**
	 * The events this activist has created.
	 */
	@OneToMany(
			targetEntity=Event.class
			)
	@JoinTable(
			name="EVENT_CREATOR_TABLE",
			joinColumns=@JoinColumn(name="ACTIVIST_ID"),
			inverseJoinColumns=@JoinColumn(name="EVENT_ID")
			)
	private Set<Event> _createdEvents = new HashSet<Event>();

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

	/**
	 * The set of events an activist is RSVPed to.
	 */
	@ManyToMany(
			targetEntity=Event.class,
			cascade={CascadeType.PERSIST, CascadeType.MERGE}
			)
	@JoinTable(
			name="RSVP_TABLE",
			joinColumns=@JoinColumn(name="ACTIVIST_ID"),
			inverseJoinColumns=@JoinColumn(name="EVENT_ID")
			)
	private Set<Event> _rsvped = new HashSet<Event>();
	
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
