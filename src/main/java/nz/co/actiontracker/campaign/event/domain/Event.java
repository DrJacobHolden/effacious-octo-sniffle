package nz.co.actiontracker.campaign.event.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
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
@Embeddable
@XmlRootElement
public class Event {
	
	protected Event() {}
	
	@Column(nullable=false)
	private String _name;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date _eventTime;
	
	//Only store attendees' unique ids as we will never
	//need to perform any action on an attendee
	@ElementCollection
	@Column(name="AttendeeID")
	protected Set<Long> attendees = new HashSet<Long>();
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Event))
			return false;
		if( obj == this)
			return true;
		
		Event a = (Event) obj;
		return new EqualsBuilder().
				append(_name, a._name).
				append(_eventTime, a._eventTime).
				isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,31).
				append(_name).
				append(_eventTime).
				toHashCode();
	}
	
}
