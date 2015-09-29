package nz.co.actiontracker;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import nz.co.actiontracker.activist.ActivistResource;

/**
 * A simple Application class that specifies the ActivistResource
 * as a singleton. In a completed implementation this would contain
 * many more resources and some classes.
 */
@ApplicationPath("/services")
public class ActionTrackerApplication extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	
	public ActionTrackerApplication() {
		singletons.add(new ActivistResource());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
	
}
