package com.forum.rest;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.forum.entities.User;
import com.google.gson.Gson;

@Path("/authentication")
public class AuthenticationEndpoint {

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticateUser(@QueryParam("username") String username, @QueryParam("password") String password) {

		try {

			// Authenticate the user using the credentials provided
			authenticate(username, password);

			// Issue a token for the user
			String token = issueToken(username);

			// Return the token on the response
			return Response.ok(token).header("Origin", "*").header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Methods", "GET").build();

		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).header("Origin", "*")
					.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET").build();
		}
	}

	private void authenticate(String username, String password) throws Exception {
		// Authenticate against a database
		boolean autenticated = false;
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("ForumApp");
		EntityManager em = factory.createEntityManager();

		TypedQuery<User> q = em.createQuery("SELECT u FROM User u WHERE u.username = '" + username + "'", User.class);
		User user = q.getSingleResult();

		if (user != null) {

			if (user.getPassword().equals(password)) {
				autenticated = true;
			}
		}

		// Close the entity manager
		em.close();
		factory.close();

		// Throw an Exception if the credentials are invalid
		if (!autenticated) {
			throw new Exception();
		}
	}

	private String issueToken(String username) throws Exception {
		// Issue a token (can be a random String persisted to a database or a JWT token)
		// The issued token must be associated to a user
		// Return the issued token
		UUID uuid = UUID.randomUUID();
		String token = uuid.toString();
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("ForumApp");
		EntityManager em = factory.createEntityManager();

		EntityTransaction trans = em.getTransaction();
		trans.begin();
		em.createQuery("UPDATE User set token = '" + token + "' WHERE username = '" + username + "'").executeUpdate();
		trans.commit();
		
		TypedQuery<User> q = em.createQuery("SELECT u FROM User u WHERE u.username = '" + username + "'", User.class);
		User user = q.getSingleResult();
		
		String json = new Gson().toJson(user);

		// Close the entity manager
		em.close();
		factory.close();

		return json;
	}

}
