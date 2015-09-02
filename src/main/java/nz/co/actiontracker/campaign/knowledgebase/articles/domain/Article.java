package nz.co.actiontracker.campaign.knowledgebase.articles.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an article in the knowledgebase.
 */
@Embeddable
@XmlRootElement
public class Article {
	
	protected Article() {}
	
	@Column(nullable=false)
	private String _name;
	
	//The link to the article
	@Column(nullable=false)
	private String _link;
	
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
				isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,31).
				append(_name).
				append(_link).
				toHashCode();
	}
}
