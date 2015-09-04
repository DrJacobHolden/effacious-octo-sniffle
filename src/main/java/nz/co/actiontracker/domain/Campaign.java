package nz.co.actiontracker.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

	@Id
	@GeneratedValue(generator="ID_GENERATOR")
	private long _id;

	@Column(unique=true, name="NAME", nullable=false)
	private String _name;
	
	public Campaign(String name) {
		_name = name;
	}
	
	protected Campaign() {}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="_id")
	protected Set<Event> events = new HashSet<Event>();
	
	@OneToOne
	@JoinColumn(name="_id")
	protected KnowledgeBase KB = new KnowledgeBase(this);
	
	public String getName() {
		return _name;
	}
	
	public long getId() {
		return _id;
	}
	
}
