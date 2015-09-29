package nz.co.actiontracker.campaign.knowledgebase;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an article in the knowledgebase.
 * 
 * Another example of a JAXB value type.
 */
@Embeddable
@XmlRootElement(name="article")
@XmlAccessorType(XmlAccessType.FIELD)
public class Article {
	
	/*
	 * Methods/Functions
	 */
	
	@Override
	public String toString() {
		return _name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Article))
			return false;
		if( obj == this)
			return true;
		
		Article a = (Article) obj;
		return new EqualsBuilder().
				append(_name, a._name).
				append(_link, a._link).
				append(_creatorId, a._creatorId).
				isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,31).
				append(_name).
				append(_link).
				append(_creatorId).
				toHashCode();
	}
	
	/*
	 * Fields
	 */
	
	/**
	 * The name of the article.
	 */
	@Column(nullable=false)
	@XmlElement(name="name")
	private String _name;
	
	/**
	 * The link to the article.
	 */
	@Column(nullable=false)
	@XmlElement(name="link")
	private String _link;
	
	/**
	 * The ID of the creator.
	 */
	@Column(nullable=false)
	@XmlElement(name="creatorId")
	private long _creatorId;
	
	/*
	 * Constructors
	 */
	
	public Article(String name, String link, long creator) {
		_name = name;
		_link = link;
		_creatorId = creator;
	}
	
	protected Article() {}
	
	/*
	 * Getters and Setters
	 */
	
	public String getLink() {
		return _link;
	}
	
	public String getName() {
		return _name;
	}
	
	public long getCreator() {
		return _creatorId;
	}
}
