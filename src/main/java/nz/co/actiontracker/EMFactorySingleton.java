package nz.co.actiontracker;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * 
 * This probably has a naming convention that I have disobeyed.
 * But this class is where the entity manager factory is created,
 * it is a singleton so there is only one. This should drastically
 * reduce overhead.
 * 
 * @author trob525
 *
 */
public class EMFactorySingleton {

	private static EntityManagerFactory instance = null;
	
	protected EMFactorySingleton() {
	}
	
	public static EntityManagerFactory getInstance() {
		if(instance == null)
			instance = Persistence.createEntityManagerFactory("scratchPU");
		return instance;
	}
	
}
