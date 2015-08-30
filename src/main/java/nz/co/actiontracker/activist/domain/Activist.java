package nz.co.actiontracker.activist.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to represent an Activist. This is created
 * by a  user if they choose to sign up and persists
 * in the database.
 * 
 * @author Tate Robertson
 */
@Entity
@XmlRootElement(name="activist")
public class Activist {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long _id;
	
	private String _username;
	
	private String _email;
	private Gender _gender;
	private int _age;
	private Address _address;
	
	public Activist(String username, String email, Gender gender, int age, Address address) {
		this._username = username;
		this._email = email;
		this._gender = gender;
		this._age = age;
		this._address = address;
	}
	
	//TODO: Create other constructors
	
	/**
	 * Create an activist with the minimum level of info
	 */
	public Activist(String username, String email) {
		this(username, email, null, 0, null);
	}
	
	protected Activist() {}
}
