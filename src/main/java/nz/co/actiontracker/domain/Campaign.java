package nz.co.actiontracker.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Represents an ActionTracker campaign. This is what a user
 * subscribes to and contains the collections of events and
 * petitions. It also contains a reference to the knowledgebase.
 * 
 * In future additional collections will need to be added and
 * tracked here. The current set is simplistic.
 *
 */
@Entity
@Table(name="CAMPAIGNS")
@XmlRootElement
public class Campaign {
	
	/*
	 * Functions/Methods
	 */
	
	/**
	 * Adds an activist as a subscriber to this campaign.
	 * 
	 * Returns false if the activist is already subscribed to the campaign.
	 */
	protected boolean addSubscriber(Activist sub) {
		if (!subscribers.contains(sub)) {
			subscribers.add(sub);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes an activist from the subscribed list of this campaign.
	 * 
	 * Returns false if the activist is not already subscribed to this campaign.
	 */
	protected boolean removeSubscriber(Activist sub) {
		if (subscribers.contains(sub)) {
			subscribers.remove(sub);
			return true;
		} else {
			return false;
		}
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
	 * The name of the campaign, this must be unique.
	 * 
	 * In future a description field would be added to
	 * further flesh out the campaign but for this simple
	 * proof of concept the name is the only field that
	 * can be used to describe the campaign.
	 */
	@Column(unique=true, name="NAME", nullable=false)
	private String _name;

	/**
	 * The creator of this campaign.
	 * 
	 * An activist can create many campaigns, but a campaign
	 * can only have one creator.
	 */
	@ManyToOne(
			targetEntity = Activist.class
			)
	private Activist _creator;
	
	/**
	 * The set of events associated with this campaign.
	 * 
	 * An event can only be associated with one campaign, but
	 * a campaign can be associated with many events.
	 */
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="_id")
	protected Set<Event> events = new HashSet<Event>();

	/**
	 * The Knowledge Base for this campaign.
	 * 
	 * There is a one-to-one relationship between knowledgebases
	 * and campaigns. Each campaign has its own unique knowledgebase.
	 */
	@OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
	protected KnowledgeBase _KB;
	
	/**
	 * The activists who are subscribed to this campaign.
	 * 
	 * Many activists can be subscribed to a campaign, and
	 * an activist can subscribe to many campaigns.
	 */
	@ManyToMany(
			cascade = {CascadeType.PERSIST, CascadeType.MERGE},
			mappedBy = "_subscribed",
			targetEntity = Activist.class
			)
	protected Set<Activist> subscribers = new HashSet<Activist>();
	
	/*
	 * Constructors
	 */

	public Campaign(String name) {
		_name = name;
		_KB = new KnowledgeBase(name);
	}

	protected Campaign() {}
	
	/*
	 * Setters and Getters
	 */
	
	public Collection<Activist> getSubscribers() {
		return subscribers;
	}
	
	public String getName() {
		return _name;
	}

	public long getId() {
		return _id;
	}

	protected void setCreator(Activist creator) {
		_creator = creator;
	}
	
	public Activist getCreator() {
		return _creator;
	}

	public KnowledgeBase getKB() {
		return _KB;
	}
}
