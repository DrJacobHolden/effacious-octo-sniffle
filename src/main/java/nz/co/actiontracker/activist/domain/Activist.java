package nz.co.actiontracker.activist.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

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
	
	@Id
	@GeneratedValue(generator="ID_GENERATOR")
	private long _id;
	
	@Column(unique=true, name="USERNAME", nullable=false)
	private String _username;
	
	@Column(name="EMAIL", nullable=false)
	private String _email;
	
	@Column(name="GENDER")
	private Gender _gender;
	
	@Column(name="AGE")
	private int _age;
	
	//
	//private Address _address;
	
	public Activist(String username, String email, Gender gender, int age, Address address) {
		this._username = username;
		this._email = email;
		this._gender = gender;
		this._age = age;
		//this._address = address;
	}
	
	//TODO: Create other constructors
	
	/**
	 * Create an activist with the minimum level of info
	 */
	public Activist(String username, String email) {
		this(username, email, null, 0, null);
	}
	
	protected Activist() {}
	
	public String getUsername() {
		return _username;
	}
	
	public long getId() {
		return _id;
	}
}
