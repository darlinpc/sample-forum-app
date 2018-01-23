package com.forum.rest;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.forum.entities.Category;
import com.forum.entities.Comment;
import com.forum.entities.Post;
import com.forum.entities.User;
import com.google.gson.Gson;

@Path("/posts")
public class PostService {

	class P {
		public String id;
		public String title;
		public String author;
		public String category;
		public String content;
	}

	private EntityManagerFactory factory = Persistence.createEntityManagerFactory("ForumApp");
	private EntityManager em = factory.createEntityManager();

	@GET
	@Path("/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPostById(@PathParam("param") String id) throws SQLException {

		TypedQuery<Post> q = em.createQuery("select p from Post p where p.id =" + id, Post.class);
		Post result = (Post) q.getSingleResult();

		// to avoid circular reference, need to find a cleaner way
		Set<Comment> comments = result.getComments();
		for (Comment comment : comments) {
			comment.setPost(null);
		}

		String json = new Gson().toJson(result);

		closeConexion();
		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(json).build();

	}

	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getLatestPosts() throws SQLException {

		Query q = em.createNativeQuery(
				"select * from Post p, User u where u.id = p.author_id order by p.createddate desc limit 10",
				Post.class);
		List<Post> results = q.getResultList();

		// to avoid circular reference, need to find a cleaner way
		for (Post post : results) {
			Set<Comment> comments = post.getComments();
			for (Comment comment : comments) {
				comment.setPost(null);
			}
		}

		String json = new Gson().toJson(results);

		closeConexion();
		return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(json).build();

	}

	@GET
	@Path("/category/{param}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getPostByCategory(@PathParam("param") String category) throws SQLException {

		Query q = em.createNativeQuery(
				"select * from Post p where p.category_id = " + category + " order by p.createddate desc", Post.class);
		List<Post> results = q.getResultList();

		// to avoid circular reference, need to find a cleaner way
		for (Post post : results) {
			Set<Comment> comments = post.getComments();
			for (Comment comment : comments) {
				comment.setPost(null);
			}
		}

		String json = new Gson().toJson(results);
		closeConexion();

		return Response.status(200).entity(json).build();

	}

	@GET
	@Path("/categories")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCategories() throws SQLException {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("ForumApp");
		EntityManager em = factory.createEntityManager();

		Query q = em.createNativeQuery("select * from Category c order by c.name", Category.class);

		List<Category> results = q.getResultList();
		String json = new Gson().toJson(results);
		closeConexion();

		return Response.status(200).entity(json).build();

	}

	@GET
	@Path("/topcategories")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getTopCategories() throws SQLException {

		Query q = em.createNativeQuery(
				"select * from (select *, count(*) count from Category c, Post p where c.id = p.category_id group by p.category_id) order by count desc limit 10",
				Category.class);

		List<Category> results = q.getResultList();
		String json = new Gson().toJson(results);
		closeConexion();

		return Response.status(200).entity(json).build();

	}

	@GET
	@Path("/addcomment")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addComment(@QueryParam("id") String id, @QueryParam("comment") String comment,
			@QueryParam("author") String author) throws SQLException {

		TypedQuery<Post> q = em.createQuery("select p from Post p where p.id =" + id, Post.class);
		Post post = (Post) q.getSingleResult();

		TypedQuery<User> q1 = em.createQuery("select u from User u where u.id =" + author, User.class);
		User user = (User) q1.getSingleResult();

		Comment c = new Comment();
		c.setContent(comment);
		c.setPost(post);
		c.setAuthor(user);
		c.setCreateddate(new Timestamp(System.currentTimeMillis()));

		EntityTransaction trans = em.getTransaction();
		trans.begin();
		em.persist(c);
		trans.commit();

		TypedQuery<Post> q2 = em.createQuery("select p from Post p where p.id =" + id, Post.class);
		post = (Post) q2.getSingleResult();

		// to avoid circular reference, need to find a cleaner way
		Set<Comment> comments = post.getComments();
		for (Comment comm : comments) {
			comm.setPost(null);
		}

		String json = new Gson().toJson(post);
		closeConexion();

		return Response.status(200).entity(json).build();

	}

	@POST
	@Path("/create")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response create(String message) throws SQLException {

		P post = new Gson().fromJson(message, P.class);

		TypedQuery<User> q = em.createQuery("select u from User u where u.id =" + post.author, User.class);
		User user = (User) q.getSingleResult();

		TypedQuery<Category> categoryQuery = em
				.createQuery("select c from Category c where c.name ='" + post.category + "'", Category.class);
		Category categoryResult = (Category) categoryQuery.getSingleResult();

		Post p = new Post();
		p.setTitle(post.title);
		p.setContent(post.content);
		p.setCategory(categoryResult);
		p.setAuthor(user);
		p.setCreatedDate(new Timestamp(System.currentTimeMillis()));

		EntityTransaction trans = em.getTransaction();
		trans.begin();
		em.persist(p);

		trans.commit();
		closeConexion();

		return Response.status(200).build();

	}

	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response save(String message) throws SQLException {

		P post = new Gson().fromJson(message, P.class);

		TypedQuery<Post> q = em.createQuery("select p from Post p where p.id =" + post.id, Post.class);
		Post p = (Post) q.getSingleResult();

		TypedQuery<Category> categoryQuery = em
				.createQuery("select c from Category c where c.name ='" + post.category + "'", Category.class);
		Category categoryResult = (Category) categoryQuery.getSingleResult();

		p.setTitle(post.title);
		p.setCategory(categoryResult);
		p.setContent(post.content);

		EntityTransaction trans = em.getTransaction();
		trans.begin();
		em.persist(p);

		trans.commit();
		closeConexion();

		return Response.status(200).build();

	}

	private void closeConexion() {
		this.em.close();
		this.factory.close();
	}

}
