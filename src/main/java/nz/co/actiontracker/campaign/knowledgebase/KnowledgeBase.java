package nz.co.actiontracker.campaign.knowledgebase;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@Entity
@XmlRootElement
public class KnowledgeBase {
	
	private static Logger _logger = LoggerFactory.getLogger(KnowledgeBase.class);
	
	/*
	 * Methods/Functions
	 */

	/**
	 * Adds a new article to the Knowledgebase.
	 * 
	 * Returns false if the article already exists in the knowledgebase.
	 */
	public boolean addArticle(String name, String link, long creator) {
		Article article = new Article(name, link, creator);
		for (Article art : _articles) {
			if(art.equals(article)) {
				_logger.error("Could not add article " + name + 
						" as it already exists.");
				return false;
			}
		}
		_articles.add(article);
		_logger.info("Successfully added article " + name + ".");
		return true;
	}
	
	/**
	 * Removes an article from the Knowledgebase.
	 * 
	 * Returns false if the article does not exist, or was not created
	 * by this activist.
	 */
	public boolean removeArticle(String name, String link, long creator) {
		Article article = new Article(name, link, creator);
		for (Article art : _articles) {
			if(art.equals(article)) {
				_articles.remove(art);
				_logger.info("Successfully removed article " + name + ".");
				return true;
			}
		}
		_logger.error("Could not remove article " + name + 
				" are you the owner?");
		return false;
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
	
	@Column(unique=true, name="NAME", nullable=false)
	private String _name;
	
	@ElementCollection
	@CollectionTable(name="ARTICLE")
	@AttributeOverride(
			name="_name",
			column = @Column(name="ARTICLE_NAME",nullable=false))
	protected Set<Article> _articles = new HashSet<Article>();
	
	/*
	 * Constructors
	 */
	
	public KnowledgeBase(long id, String name, Set<Article> articles) {
		_id = id;
		_name = name;
		_articles = articles;
	}
	
	public KnowledgeBase() {}
	
	/*
	 * Getters and Setters
	 */
	
	public void setName(String name) {
		_name = name;
	}
	
	public Set<Article> getArticles() {
		return _articles;
	}
	
}
