package it.vige.rubia.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import it.vige.rubia.ModuleException;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Topic;
import it.vige.rubia.search.ForumsSearchModule;
import it.vige.rubia.search.ResultPage;
import it.vige.rubia.search.SearchCriteria;

@Path("/search/")
public class RestForumsSearchModule implements ForumsSearchModule {

	@EJB
	private ForumsSearchModule forumsSearchModule;

	@POST
	@Path("findPosts")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public ResultPage<Post> findPosts(SearchCriteria criteria) throws ModuleException {
		// TODO Auto-generated method stub
		return forumsSearchModule.findPosts(criteria);
	}

	@POST
	@Path("findTopics")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public ResultPage<Topic> findTopics(SearchCriteria criteria) throws ModuleException {
		// TODO Auto-generated method stub
		return forumsSearchModule.findTopics(criteria);
	}

}
