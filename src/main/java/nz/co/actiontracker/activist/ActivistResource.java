package nz.co.actiontracker.activist;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.co.actiontracker.EMFactorySingleton;
import nz.co.actiontracker.event.Event;
import nz.co.actiontracker.event.EventDTO;
import nz.co.actiontracker.event.EventMapper;

/**
 * 
 * The Activist resource. Using this URI you can create a new activist,
 * update an activist, view an activist and subscribe to activists being
 * created.
 * 
 * You are also able to view events an activist is attending and campaigns
 * an activist is subscribed to. (Not implemented)
 * 
 * It is not possible to delete an Activist due to technical restrictions.
 * Seriously, it would create a huge mess and just isn't worth it. The NSA
 * have probably saved all your data anyway so a delete could just send back
 * "Yes I have deleted your account" and really do nothing.
 */
@Path("/activists")
public class ActivistResource {

	private static Logger _logger = LoggerFactory.getLogger(ActivistResource.class);
	
	private EntityManagerFactory EMF = EMFactorySingleton.getInstance();
	
	private List<AsyncResponse> responses = new ArrayList<AsyncResponse>();

	/**
	 * Adds an activist to the database.
	 * 
	 * This also notifies all those subscribed that a new activist
	 * has been added to the database.
	 */
	@POST
	@Consumes("application/xml")
	public synchronized Response signUp(ActivistDTO a) {
		EntityManager em = EMF.createEntityManager();
		Activist activist = ActivistMapper.toDomainModel(a);
		em.getTransaction().begin();
		em.persist(activist);
		em.getTransaction().commit();
		em.close();
		
		for (AsyncResponse response : responses) {
			response.resume("An Activist with the username: " + a.get_username() + " has been created within the database.");
		}
		responses.clear();
		
		return Response.created(URI.create("/activists/" + activist.getId())).build();
	}

	/**
	 * Returns an activist specified by an ID
	 */
	@GET
	@Path("/{id}")
	@Produces("application/xml")
	public ActivistDTO retrieveActivist(@PathParam("id") long id) {
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		Activist a = em.find(Activist.class, id);
		if (a == null) {
			throw new WebApplicationException(404);
		}
		em.getTransaction().commit();
		em.close();
		return ActivistMapper.toDTO(a);
	}
	
	/**
	 * Returns a list of activists, this supports limited query parameters,
	 * allowing you to specify an id range in the database.
	 * 
	 * It should probably be noted that this: http://www.mkyong.com/webservices/jax-rs/jax-rs-queryparam-example/
	 * was of immense help when working out how to do this.
	 */
	@GET
	@Produces("application/xml")
	public List<ActivistDTO> retrieveActivists(@Context UriInfo info) {
		
		long rangeFrom = 0;
		long rangeTo = Long.MAX_VALUE;
		String from = info.getQueryParameters().getFirst("from");
		String to = info.getQueryParameters().getFirst("to");
		if(from != null) {
			rangeFrom = Long.parseLong(from);
		}
		if(to != null) {
			rangeTo = Long.parseLong(to);
		}
		
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		List<Activist> list = em.createQuery("select activist_ from Activist activist_ where activist_.id > " + rangeFrom + " AND activist_.id < " + rangeTo).getResultList();
		List<ActivistDTO> listActivists = new ArrayList<ActivistDTO>();
		for (int i = 0; i < list.size(); i++) {
			listActivists.add(ActivistMapper.toDTO(list.get(i)));
		}
		em.getTransaction().commit();
		em.close();
		return listActivists;
	}
	
	/**
	 * This is a simple and poor implementation of an async response.
	 * That utilises a long poll GET.
	 * 
	 * This has been used for the sake of KISS as it is unlikely many
	 * activists will ever sign up simultaneously so the case of missing
	 * an activist is unlikely.
	 * 
	 * This produces a plaintext response.
	 */
	@GET
	@Path("/subscribe")
	@Produces("text/plain")
	public synchronized void subscribeActivists(@Suspended AsyncResponse response) {
		_logger.info("A user has subscribed to Activists.");
		responses.add(response);
	}
	
	/**
	 * Returns all the events an activist is attending. This is provided
	 * as an example of how this request would work. It is not actually
	 * usable in a test case as the Event and Campaign resources are not
	 * implemented.
	 */
	@GET
	@Path("/{id}/events")
	@Produces("application/xml")
	public List<EventDTO> retrieveActivistEvents(@PathParam("id") long id) {
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		Activist a = em.find(Activist.class, id);
		if (a == null) {
			throw new WebApplicationException(404);
		}
		List<EventDTO> listEvents = new ArrayList<EventDTO>();
		for (Event e : a.get_rsvped()) {
			listEvents.add(EventMapper.toDTO(e));
		}
		em.getTransaction().commit();
		em.close();
		return listEvents;
	}
	
	/**
	 * Updates an activist specified by an id.
	 */
	@PUT
	@Path("/{id}")
	@Consumes("application/xml")
	public void updateActivist(@PathParam("id") long id, ActivistDTO a) {
		EntityManager em = EMF.createEntityManager();
		em.getTransaction().begin();
		Activist activist = em.find(Activist.class, id);
		if (activist == null) {
			throw new WebApplicationException(404);
		}
		//Update the username
		if (a.get_username() != null && !activist.getUsername().equals(a.get_username())) {
			activist.setUsername(a.get_username());
		}
		//Update the email
		if (a.get_email() != null && !activist.getEmail().equals(a.get_email())) {
			activist.setEmail(a.get_email());
		}
		//Update the address
		if (a.get_address() != null && !activist.getAddress().equals(a.get_address())) {
			activist.setAddress(a.get_address());
		}
		em.persist(activist);
		em.getTransaction().commit();
		em.close();
		return;
	}

}
