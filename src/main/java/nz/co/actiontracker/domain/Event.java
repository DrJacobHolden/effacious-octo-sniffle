package nz.co.actiontracker.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	
	@Override
	public String toString() {
		return _name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Event))
			return false;
		if( obj == this)
			return true;
		
		Event a = (Event) obj;
		return new EqualsBuilder().
				append(_name, a._name).
				append(_eventDate, a._eventDate).
				append(_creator, a._creator).
				append(_campaign, a._campaign).
				append(_location, a._location).
				isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,31).
				append(_name).
				append(_eventDate).
				append(_creator).
				append(_location).
				append(_campaign).
				toHashCode();
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
	 * The IDs of the attending activists
	 */
	@ElementCollection
	@Column(name="AttendeeID")
	private Set<Long> attendees = new HashSet<Long>();
	
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
