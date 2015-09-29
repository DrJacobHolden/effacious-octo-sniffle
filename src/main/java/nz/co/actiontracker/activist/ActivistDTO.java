package nz.co.actiontracker.activist;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A DTO class representing a simplified Activist.
 * 
 * This class removes all the collection relationships as
 * these are retrieved using separate API requests. This just
 * contains the details about an Activist.
 */
@XmlRootElement(name="activist")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivistDTO {

	@XmlAttribute(name="id")
	private long _id;
	
	@XmlElement(name="username")
	private String _username;
	
	@XmlElement(name="email")
	private String _email;
	
	@XmlElement(name="address")
	private Address _address;
	
	public ActivistDTO(String username, String email, Address address) {
		this._username = username;
		this._email = email;
		this._address = address;
	}
	
	public ActivistDTO(long id, String username, String email, Address address) {
		this._id = id;
		this._username = username;
		this._email = email;
		this._address = address;
	}
	
	protected ActivistDTO() {}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String get_username() {
		return _username;
	}

	public void set_username(String _username) {
		this._username = _username;
	}

	public String get_email() {
		return _email;
	}

	public void set_email(String _email) {
		this._email = _email;
	}

	public Address get_address() {
		return _address;
	}

	public void set_address(Address _address) {
		this._address = _address;
	}
	
}
