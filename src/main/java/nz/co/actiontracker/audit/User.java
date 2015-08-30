package nz.co.actiontracker.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nz.co.actiontracker.activist.domain.Activist;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long _id;
	
	private Activist _account;
	
	@Column(unique=true)
	private String _username;
	
	public User(String username) {
		this._username = username;
	}
	
	protected User() {}
	
	public Long getId() {
		return _id;
	}
	
	public String getUsername() {
		return _username;
	}
	
	public Activist getAccount() {
		return _account;
	}
	
	public void signUp(Activist account) {
		this._account = account;
	}
}
