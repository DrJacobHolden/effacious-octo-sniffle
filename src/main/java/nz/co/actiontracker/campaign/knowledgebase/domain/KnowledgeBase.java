package nz.co.actiontracker.campaign.knowledgebase.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

import nz.co.actiontracker.campaign.knowledgebase.articles.domain.Article;

/**
 * 
 * Represents a Knowledge Base. This is the container for all
 * the articles, videos, polls and various other information
 * relevant to the campaign.
 * 
 * In this implementation this is very basic and would not be
 * suitable for an actual application. This is mainly to demonstrate
 * various levels of hierarchies within the database.
 * 
 * @author trob525
 *
 */
@Embeddable
@XmlRootElement
public class KnowledgeBase {

	protected KnowledgeBase() {}
	
	@ElementCollection
	@CollectionTable(name="ARTICLE")
	@AttributeOverride(
			name="_name",
			column = @Column(name="ARTICLE_NAME",nullable=false))
	protected Set<Article> articles = new HashSet<Article>();
	
}
