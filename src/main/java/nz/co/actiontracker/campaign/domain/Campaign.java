package nz.co.actiontracker.campaign.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import nz.co.actiontracker.campaign.event.domain.Event;
import nz.co.actiontracker.campaign.knowledgebase.domain.KnowledgeBase;

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
@XmlRootElement
public class Campaign {

	@Id
	@GeneratedValue(generator="ID_GENERATOR")
	private long _id;

	@Column(unique=true)
	private String _name;
	
	public Campaign(String name) {
		_name = name;
		KB = new KnowledgeBase(this);
	}
	
	protected Campaign() {}
	
	@ElementCollection
	@CollectionTable(name="EVENTS")
	@AttributeOverride(
			name="_name",
			column = @Column(name="EVENT_NAME",nullable=false))
	protected Set<Event> events = new HashSet<Event>();
	
	protected KnowledgeBase KB;
	
	public String getName() {
		return _name;
	}
	
	public long getId() {
		return _id;
	}
	
}
