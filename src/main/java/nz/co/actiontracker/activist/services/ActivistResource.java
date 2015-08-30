package nz.co.actiontracker.activist.services;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

@Path("/activist")
public interface ActivistResource {

	/**
	 * Handles incoming HTTP POST requests for the relative URI "activist", 
	 * where the request body contains XML data. This method signs a new 
	 * user up with an activist account.
	 * @param stores the HTTP request message body. This is a regular 
	 * java.io.InputStream object and is expected to hold a XML representation
	 * of the activist account to create, 
	 * @return a JAX-RS Response object that the JAX-RS implementation uses to
	 * prepare the HTTP response message.
	 */
	@POST
	@Consumes("application/xml")
	Response signUp(InputStream is);

	/**
	 * Returns the activist account of the current user.
	 * @return a StreamingOutput object storing a representation of the required
	 *         Activist in XML format.
	 */
	@GET
	@Produces("application/xml")
	StreamingOutput viewSelf();

	/**
	 * Handles incoming HTTP GET requests for the relative URI "activist/{id}.
	 * @param id the unique id of the activist to retrieve.
	 * @return a StreamingOutput object storing a representation of the required
	 *         activist in XML format.
	 */
	@GET
	@Path("{id}")
	@Produces("application/xml")
	StreamingOutput retrieveActivist(@PathParam("id") int id);

}
