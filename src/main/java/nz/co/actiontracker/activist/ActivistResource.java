package nz.co.actiontracker.activist;

import java.io.InputStream;
import java.net.URI;
import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		_logger.info("MAKING A THING");
		return Response.created(URI.create("/activists/" + activist.getId())).build();
	}

	@GET
	@Path("/{id}")
	@Produces("application/xml")
	public ActivistDTO retrieveActivist(@PathParam("id") int id) {
		EntityManager em = Persistence.createEntityManagerFactory("scratchPU").createEntityManager();
		em.getTransaction().begin();
		Activist a = em.find(Activist.class, id);
		if (a == null) {
			System.out.println("Can not find Activist with ID: " + id);
			return null;
		}
		em.getTransaction().commit();
		return ActivistMapper.toDTO(a);
	}

}
