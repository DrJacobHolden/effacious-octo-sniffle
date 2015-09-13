package nz.co.actiontracker.event;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name="event")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventDTO {

	@XmlAttribute(name="id")
	private long _id;
	
	@XmlElement(name="name")
	private String _name;
	
	@XmlElement(name="date")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date _eventDate;
	
	@XmlElement(name="location")
	private String _location;
	
	/**
	 * For this representation we only store the creator id
	 */
	@XmlElement(name="creator")
	private long _creator;
	
	/**
	 * For this representation we only store the campaign id
	 */
	@XmlElement(name="campaign")
	private long _campaign;
	
	/**
	 * Only the store the attendees usernames, this is
	 * fine as you will never be creating or updating this
	 * set through this XML form and will instead use a specific
	 * endpoint.
	 */
	@XmlElement(name="attendee_username")
	@XmlElementWrapper(name="attendees")
	private Set<String> _attendees = new HashSet<String>();
	
	public EventDTO(long id, String name, Date date, String location, long creator, long campaign, Set<String> attendees) {
		_id = id;
		_name = name;
		_eventDate = date;
		_location = location;
		_creator = creator;
		_campaign = campaign;
		_attendees = attendees;
	}
	
	public EventDTO(String name, Date date, String location, long creator, long campaign, Set<String> attendees) {
		_name = name;
		_eventDate = date;
		_location = location;
		_creator = creator;
		_campaign = campaign;
		_attendees = attendees;
	}
	
	protected EventDTO() {}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public Date get_eventDate() {
		return _eventDate;
	}

	public void set_eventDate(Date _eventDate) {
		this._eventDate = _eventDate;
	}

	public String get_location() {
		return _location;
	}

	public void set_location(String _location) {
		this._location = _location;
	}

	public long get_creator() {
		return _creator;
	}

	public void set_creator(long _creator) {
		this._creator = _creator;
	}

	public long get_campaign() {
		return _campaign;
	}

	public void set_campaign(long _campaign) {
		this._campaign = _campaign;
	}

	public Set<String> get_attendees() {
		return _attendees;
	}

	public void set_attendees(Set<String> _attendees) {
		this._attendees = _attendees;
	}
	
}
