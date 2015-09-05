package nz.co.actiontracker.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Represents an Event such as a protest or a rally.
 *
 * An event has a name, a date and a list of attendees.
 *
 */
@Entity
@Table(name="EVENTS")
@XmlRootElement
public class Event {
	
	/*
	 * Methods/Functions
	 */
	
	/**
	 * RSVP an activist with this event.
	 * 
	 * Returns false if the activist is already rsvped.
	 */
	public boolean rsvp(Activist a) {
		if(_attendees.contains(a)) {
			return false;
		}
		_attendees.add(a);
		return true;
	}
	
	/**
	 * Remove an activist from this event.
	 * 
	 * Returns false if the activist is not rsvped.
	 */
	public boolean unrsvp(Activist a) {
		if(!_attendees.contains(a)) {
			return false;
		}
		_attendees.remove(a);
		return true;
	}
	
	@Override
	public String toString() {
		return _name;
	}
	
	/*
	 * Fields
	 */
	
	@Id
	@GeneratedValue(generator="ID_GENERATOR")
	private long _id;
	
	/**
	 * The name of the event
	 */
	@Column(name="EVENT_NAME", nullable=false)
	private String _name;
	
	/**
	 * The date of the event
	 */
	@Temporal(TemporalType.DATE)
	@Column(name="EVENT_DATE", nullable=false)
	private Date _eventDate;
	
	/**
	 * The location of the event, stored as a string to
	 * increase flexibility.
	 */
	@Column(name="EVENT_LOCATION", nullable=false)
	private String _location;
	
	/**
	 * The creator of this event.
	 * 
	 * An activist can create many events, but an event
	 * can only have one creator.
	 */
	@ManyToOne(
			targetEntity = Activist.class
			)
	private Activist _creator;

	/**
	 * The campaign this event belongs to.
	 * 
	 * A campaign can have many events, but an event
	 * can only belong to one campaign.
	 */
	@ManyToOne(
			targetEntity = Campaign.class
			)
	private Campaign _campaign;
	
	/**
	 * The activists who are subscribed to this campaign.
	 * 
	 * Many activists can be subscribed to a campaign, and
	 * an activist can subscribe to many campaigns.
	 */
	@ManyToMany(
			cascade = {CascadeType.PERSIST, CascadeType.MERGE},
			mappedBy = "_rsvped",
			targetEntity = Activist.class
			)
	private Set<Activist> _attendees = new HashSet<Activist>();
	
	/*
	 * Constructors
	 */
	
	public Event(String name, Date date, String loc) {
		_name = name;
		_eventDate = date;
		_location = loc;
	}
	
	protected Event() {}
	
	/*
	 * Getters and setters
	 */
	
	public Activist getCreator() {
		return _creator;
	}
	
	public Campaign getCampaign() {
		return _campaign;
	}
	
	protected void setCreator(Activist a) {
		_creator = a;
	}

	protected void setCampaign(Campaign c) {
		_campaign = c;
	}
}
