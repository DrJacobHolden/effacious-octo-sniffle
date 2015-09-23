package nz.co.actiontracker.activist;

import java.io.InputStream;
import java.net.URI;
import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * The Activist resource. Using this URI you can create a new activist,
 * update an activist and view an activist. You are also able to view
 * events an activist is attending and campaigns an activist is subbed
 * to.
 * 
 * It is not possible to delete an Activist due to technical restrictions.
 * Seriously, it would create a huge mess and just isn't worth it. The NSA
 * have probably saved all your data anyway so a delete could just send back
 * "Yes I have deleted your account" and really do nothing.
 * 
 * @author Tate
 *
 */
@Path("/activists")
public class ActivistResource {

	private static Logger _logger = LoggerFactory.getLogger(ActivistResource.class);

	@POST
	@Consumes("application/xml")
	public Response signUp(ActivistDTO a) {
		EntityManager em = Persistence.createEntityManagerFactory("scratchPU").createEntityManager();
		Activist activist = ActivistMapper.toDomainModel(a);
		em.getTransaction().begin();
		em.persist(activist);
		em.getTransaction().commit();
		em.close();
		return Response.created(URI.create("/activists/" + activist.getId())).build();
	}

	@GET
	@Path("/{id}")
	@Produces("application/xml")
	public ActivistDTO retrieveActivist(@PathParam("id") long id) {
		EntityManager em = Persistence.createEntityManagerFactory("scratchPU").createEntityManager();
		em.getTransaction().begin();
		Activist a = em.find(Activist.class, id);
		if (a == null) {
			throw new WebApplicationException(404);
		}
		em.getTransaction().commit();
		em.close();
		return ActivistMapper.toDTO(a);
	}
	
	@PUT
	@Path("/{id}")
	@Consumes("application/xml")
	public void updateActivist(@PathParam("id") long id, ActivistDTO a) {
		EntityManager em = Persistence.createEntityManagerFactory("scratchPU").createEntityManager();
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
