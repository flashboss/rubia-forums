/******************************************************************************
 * Vige, Home of Professional Open Source Copyright 2010, Vige, and           *
 * individual contributors by the @authors tag. See the copyright.txt in the  *
 * distribution for a full listing of individual contributors.                *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may    *
 * not use this file except in compliance with the License. You may obtain    *
 * a copy of the License at http://www.apache.org/licenses/LICENSE-2.0        *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 ******************************************************************************/
package it.vige.rubia.resttest.forumsearchmodule.test;

import static it.vige.rubia.dto.TopicType.NORMAL;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.jboss.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import it.vige.rubia.dto.CategoryBean;
import it.vige.rubia.dto.CategoryRequestBean;
import it.vige.rubia.dto.ForumBean;
import it.vige.rubia.dto.ForumInstanceBean;
import it.vige.rubia.dto.MessageBean;
import it.vige.rubia.dto.PollBean;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.resttest.RestCaller;
import it.vige.rubia.search.ResultPage;
import it.vige.rubia.search.SearchCriteria;

public class SearchTest extends RestCaller {

	private final static String url = "http://localhost:8080/rubia-forums-rest/services/search/";
	private final static String authorization = "Basic cm9vdDpndG4=";

	private static Logger log = getLogger(SearchTest.class);

	private static Date firstDate;

	private static Date secondDate;

	private static Date thirdDate;

	private static Date forthDate;

	@BeforeAll
	public static void setUp() {
		log.debug("started test");
		ForumInstanceBean forumInstanceBean = new ForumInstanceBean();
		forumInstanceBean.setId(1);

		CategoryBean categoryBean = new CategoryBean("First Test Category");
		categoryBean.setForumInstance(forumInstanceBean);
		Response response = post(url + "createCategory", authorization, categoryBean);
		CategoryBean category = response.readEntity(CategoryBean.class);

		ForumBean forumBean = new ForumBean("First Test Forum", "First Test Description", category);
		response = post(url + "createForum", authorization, forumBean);
		ForumBean forum = response.readEntity(ForumBean.class);

		PostBean postBean = new PostBean("First Test Body");
		firstDate = new Date();
		postBean.setCreateDate(firstDate);
		TopicBean topicBean = new TopicBean(forum, "First Test Topic", asList(new PostBean[] { postBean }), NORMAL,
				null);
		PosterBean poster = new PosterBean("root");
		topicBean.setPoster(poster);
		topicBean.setPoll(new PollBean());
		response = post(url + "createTopic", authorization, topicBean);
		PostBean post = response.readEntity(PostBean.class);
		topicBean = post.getTopic();
		postBean = new PostBean();
		postBean.setTopic(topicBean);
		postBean.setMessage(new MessageBean("First Test Post"));
		postBean.setPoster(post.getPoster());
		secondDate = new Date();
		postBean.setCreateDate(secondDate);
		response = post(url + "createPost", authorization, postBean);
		post = response.readEntity(PostBean.class);
		poster = post.getPoster();
		topicBean = post.getTopic();
		poster = topicBean.getPoster();
		forumBean = topicBean.getForum();
		categoryBean = forumBean.getCategory();
		topicBean = forumBean.getTopics().get(0);
		poster = topicBean.getPoster();

		thirdDate = new Date();
		postBean.setCreateDate(thirdDate);
		postBean.setMessage(new MessageBean("Second Test Post"));
		response = post(url + "createPost", authorization, postBean);
		post = response.readEntity(PostBean.class);
		poster = post.getPoster();
		assertNotEquals(0, poster.getId());
		topicBean = post.getTopic();
		poster = topicBean.getPoster();
		forumBean = topicBean.getForum();
		categoryBean = forumBean.getCategory();
		topicBean = forumBean.getTopics().get(0);
		poster = topicBean.getPoster();

		forthDate = new Date();
		postBean.setCreateDate(forthDate);
		postBean.setMessage(new MessageBean("Third Test Post"));
		response = post(url + "createPost", authorization, postBean);
		post = response.readEntity(PostBean.class);
		poster = post.getPoster();
		topicBean = post.getTopic();
		poster = topicBean.getPoster();
		forumBean = topicBean.getForum();
		categoryBean = forumBean.getCategory();
		topicBean = forumBean.getTopics().get(0);
		poster = topicBean.getPoster();
		response.close();
	}

	@Test
	public void search() {
		SearchCriteria searchCriteria = new SearchCriteria();
		Response response = post(url + "findPosts", authorization, searchCriteria);
		ResultPage<PostBean> posts = response.readEntity(new GenericType<ResultPage<PostBean>>() {
		});
		response = post(url + "findTopics", authorization, searchCriteria);
		ResultPage<TopicBean> topics = response.readEntity(new GenericType<ResultPage<TopicBean>>() {
		});
		response.close();
	}

	@AfterAll
	public static void stop() {
		log.debug("stopped test");

		Response response = get(url + "findCategories/1", authorization);
		List<CategoryBean> categoryBeans = response.readEntity(new GenericType<List<CategoryBean>>() {
		});

		CategoryRequestBean categoryRequestBean = new CategoryRequestBean();
		categoryRequestBean.setCategory(categoryBeans.stream().filter(x -> x.getTitle().equals("First Test Category"))
				.collect(toList()).get(0));
		categoryRequestBean.setLimit(3);

		response = post(url + "findPostsFromCategoryDesc", authorization, categoryRequestBean);
		List<PostBean> postBeans = response.readEntity(new GenericType<List<PostBean>>() {
		});
		assertEquals(3, postBeans.size(), "Posts size");

		response = get(url + "removePost/" + postBeans.get(0).getId() + "/false", authorization);
		response = get(url + "removePost/" + postBeans.get(1).getId() + "/false", authorization);
		response = get(url + "removePost/" + postBeans.get(2).getId() + "/true", authorization);

		response = post(url + "findPostsFromCategoryDesc", authorization, categoryRequestBean);
		postBeans = response.readEntity(new GenericType<List<PostBean>>() {
		});

		response = get(url + "findTopics/1", authorization);
		List<TopicBean> topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});
		PosterBean posterBean = topicBeans.get(0).getPoster();

		topicBeans.forEach(x -> {
			get(url + "removeTopic/" + x.getId(), authorization);
		});
		response = get(url + "findTopics/1", authorization);
		topicBeans = response.readEntity(new GenericType<List<TopicBean>>() {
		});

		response = get(url + "removePoster/" + posterBean.getId(), authorization);
		PosterBean removedPosterBean = response.readEntity(PosterBean.class);

		response = get(url + "findPosterByUserId/" + removedPosterBean.getUserId(), authorization);
		posterBean = response.readEntity(PosterBean.class);

		response = get(url + "findForums/1", authorization);
		List<ForumBean> forumBeans = response.readEntity(new GenericType<List<ForumBean>>() {
		});

		response = get(url + "findCategories/1", authorization);
		categoryBeans = response.readEntity(new GenericType<List<CategoryBean>>() {
		});

		get(url + "removeForum/" + forumBeans.get(1).getId(), authorization);
		get(url + "removeCategory/" + categoryBeans.get(1).getId(), authorization);
		response.close();
	}
}
