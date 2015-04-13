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
package it.vige.rubia.feeds;

import static it.vige.rubia.feeds.FeedConstants.ATOM;
import static it.vige.rubia.feeds.FeedConstants.CATEGORY;
import static it.vige.rubia.feeds.FeedConstants.FORUM;
import static it.vige.rubia.feeds.FeedConstants.GLOBAL;
import static it.vige.rubia.feeds.FeedConstants.POST_LIMIT;
import static it.vige.rubia.feeds.FeedConstants.RSS;
import static it.vige.rubia.feeds.FeedConstants.TOPIC;
import static it.vige.rubia.feeds.FeedConstants.URL_INIT_PARAM_NAME;
import static it.vige.rubia.feeds.FeedConstants.URL_TYPE_INIT_PARAM_NAME;
import static it.vige.rubia.feeds.FeedConstants.WRONG_FEED_REQ;
import static it.vige.rubia.feeds.FeedConstants.WRONG_FEED_SHOW_TYPE;
import static it.vige.rubia.feeds.FeedConstants.WRONG_FEED_TYPE;
import static it.vige.rubia.ui.Constants.BY;
import static it.vige.rubia.ui.Constants.p_categoryId;
import static it.vige.rubia.ui.Constants.p_forumId;
import static it.vige.rubia.ui.Constants.p_postId;
import static it.vige.rubia.ui.Constants.p_topicId;
import static it.vige.rubia.ui.PortalUtil.VIEW;
import static it.vige.rubia.ui.PortalUtil.getIdForName;
import static it.vige.rubia.ui.PortalUtil.getNameForId;
import static java.lang.Integer.valueOf;
import static java.net.URLEncoder.encode;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
import it.vige.rubia.ForumsModule;
import it.vige.rubia.ModuleException;
import it.vige.rubia.model.Category;
import it.vige.rubia.model.Forum;
import it.vige.rubia.model.Post;
import it.vige.rubia.model.Topic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * Servlet used for showing RSS entries
 * 
 * @author tomaszszymanski
 * @author Ryszard Kozmik <a href="mailto:ryszard.kozmik@jboss.com">Ryszard
 *         Kozmik</a>
 * 
 */
public class FeedsServlet extends HttpServlet {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Inject
	private ForumsModule forumsModule;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String[] uri = request.getRequestURI().split("/");

			if (uri.length < 5 || (uri[4].equals(GLOBAL) && uri.length != 5)
					|| (!uri[4].equals(GLOBAL) && uri.length != 6)) {
				response.sendError(SC_BAD_REQUEST, WRONG_FEED_REQ);
				return;
			}

			String type = uri[3];
			String what = uri[4];

			Integer id = null;

			if (!what.equals(GLOBAL))
				id = valueOf(uri[5]);

			SyndFeed feed = new SyndFeedImpl();

			if (!setFeedType(feed, type)) {
				response.sendError(SC_BAD_REQUEST, WRONG_FEED_TYPE + type);
				return;
			}

			String url = null;
			String urlType = null;

			String urlRequestParam = request.getParameter("url");
			String urlTypeRequestParam = request.getParameter("urlType");

			if (urlRequestParam != null
					&& urlRequestParam.trim().length() != 0
					&& urlTypeRequestParam != null
					&& urlTypeRequestParam.trim().length() != 0
					&& (urlTypeRequestParam.compareTo("p") == 0 || urlTypeRequestParam
							.compareTo("s") == 0)) {
				url = request.getParameter("url");
				urlType = urlTypeRequestParam;
			}

			String urlInitParam = getServletContext().getInitParameter(
					URL_INIT_PARAM_NAME);
			String urlTypeInitParam = getServletContext().getInitParameter(
					URL_TYPE_INIT_PARAM_NAME);
			if (urlInitParam != null && urlInitParam.trim().length() != 0
					&& urlTypeInitParam != null
					&& urlTypeInitParam.trim().length() != 0) {
				url = urlInitParam;
				urlType = urlTypeInitParam;
			}

			if (url == null || urlType == null) {
				response.sendError(SC_BAD_REQUEST, WRONG_FEED_REQ);
				return;
			}

			try {
				if (what.equals(CATEGORY)) {
					createCategoryFeed(feed, id, url, urlType);
				} else if (what.equals(FORUM)) {
					createForumFeed(feed, id, url, urlType);
				} else if (what.equals(TOPIC)) {
					createTopicFeed(feed, id, url, urlType);
				} else if (what.equals(GLOBAL)) {
					createGlobalFeed(feed, id, url, urlType);
				} else {
					response.sendError(SC_BAD_REQUEST, WRONG_FEED_SHOW_TYPE
							+ what);
					return;
				}
			} catch (ModuleException e) {
				response.sendError(SC_INTERNAL_SERVER_ERROR, e.getMessage());
				return;
			}
			response.setContentType("text/xml");

			Writer writer = response.getWriter();

			SyndFeedOutput output = new SyndFeedOutput();
			output.output(feed, writer);

			writer.flush();

		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

	private void createGlobalFeed(SyndFeed feed, Integer id, String url,
			String urlType) throws ModuleException {
		feed.setTitle("Rubia Forums Global Feed");
		feed.setLink(globalLink(url, urlType));
		feed.setDescription("Messages posted in Rubia Forums");

		List<SyndEntry> entries = new ArrayList<SyndEntry>();

		List<Post> posts = forumsModule.findPostsDesc(POST_LIMIT);

		for (int i = 0; i < posts.size(); i++) {
			entries.add(getEntry(posts.get(i), url, urlType));
		}

		feed.setEntries(entries);
	}

	private void createForumFeed(SyndFeed feed, Integer id, String url,
			String urlType) throws ModuleException {

		Forum forum = forumsModule.findForumById(id);

		feed.setTitle("Rubia Forums Forum Feed: " + forum.getName());
		feed.setLink(forumLink(id.toString(), url, urlType));
		feed.setDescription("Messages posted in forum " + forum.getName()
				+ " in category " + forum.getCategory().getTitle());

		List<SyndEntry> entries = new ArrayList<SyndEntry>();

		List<Post> posts = forumsModule.findPostsFromForumDesc(forum,
				POST_LIMIT);

		for (int i = 0; i < posts.size(); i++) {
			entries.add(getEntry(posts.get(i), url, urlType));
		}

		feed.setEntries(entries);

	}

	private void createTopicFeed(SyndFeed feed, Integer id, String url,
			String urlType) throws ModuleException {

		Topic topic = forumsModule.findTopicById(id);

		feed.setTitle("Rubia Forums Topic Feed: " + topic.getSubject());
		feed.setLink(topicLink(id.toString(), url, urlType));
		feed.setDescription("Messages posted in topic " + topic.getSubject()
				+ " in forum " + topic.getForum().getName() + " in category "
				+ topic.getForum().getCategory().getTitle());

		List<SyndEntry> entries = new ArrayList<SyndEntry>();

		List<Post> posts = forumsModule.findPostsByTopicId(topic);

		for (int i = 0; i < posts.size(); i++) {
			entries.add(getEntry(posts.get(i), url, urlType));
		}

		feed.setEntries(entries);
	}

	private boolean setFeedType(SyndFeed feed, String type) {
		if (type.equals(RSS)) {
			feed.setFeedType("rss_2.0");
		} else if (type.equals(ATOM)) {
			feed.setFeedType("atom_0.3");
		} else {
			return false;
		}
		return true;
	}

	private void createCategoryFeed(SyndFeed feed, Integer id, String url,
			String urlType) throws ModuleException {

		Category category = forumsModule.findCategoryById(id);

		feed.setTitle("Rubia Forums Category Feed: " + category.getTitle());
		feed.setLink(categoryLink(id.toString(), url, urlType));
		feed.setDescription("Messages posted in category "
				+ category.getTitle());

		List<SyndEntry> entries = new ArrayList<SyndEntry>();

		List<Post> posts = forumsModule.findPostsFromCategoryDesc(category,
				POST_LIMIT);

		for (int i = 0; i < posts.size(); i++) {
			entries.add(getEntry(posts.get(i), url, urlType));
		}

		feed.setEntries(entries);
	}

	private SyndEntry getEntry(Post post, String url, String urlType) {
		SyndEntry entry;
		SyndContent description;

		entry = new SyndEntryImpl();
		entry.setTitle(post.getMessage().getSubject() + BY
				+ post.getPoster().getUserId());
		entry.setLink(postLink(post.getId().toString(), url, urlType));
		entry.setPublishedDate(post.getCreateDate());
		description = new SyndContentImpl();
		description.setType("text/html");

		String text = post.getMessage().getText();
		description
				.setValue(post.getMessage().getHTMLEnabled() ? escapeHtml(text)
						: text);
		entry.setDescription(description);

		return entry;
	}

	private String postLink(String id, String url, String urlType) {
		String viewUrl = getNameForId("t");
		String completeUrl = buildCompleteUrl(url, urlType, viewUrl);
		return completeUrl + p_postId + "=" + id + "#" + id;
	}

	private String topicLink(String id, String url, String urlType) {
		String viewUrl = getNameForId("t");
		String completeUrl = buildCompleteUrl(url, urlType, viewUrl);
		return completeUrl + p_topicId + "=" + id;
	}

	private String forumLink(String id, String url, String urlType) {
		String viewUrl = getNameForId("f");
		String completeUrl = buildCompleteUrl(url, urlType, viewUrl);
		return completeUrl + p_forumId + "=" + id;
	}

	private String categoryLink(String id, String url, String urlType) {
		return globalLink(url, urlType) + p_categoryId + "=" + id;
	}

	private String globalLink(String url, String urlType) {
		String viewUrl = getNameForId("c");
		return buildCompleteUrl(url, urlType, viewUrl);
	}

	private String buildCompleteUrl(String url, String urlType, String viewUrl) {
		if (urlType.compareTo("p") == 0) {
			try {
				url += "&" + VIEW + "="
						+ encode(getIdForName(viewUrl), "UTF-8") + "&";
			} catch (UnsupportedEncodingException e) {
			}
		} else {
			url += viewUrl + "?";
		}

		return url;
	}

}
