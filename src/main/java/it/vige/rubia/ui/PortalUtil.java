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
package it.vige.rubia.ui;

import static it.vige.rubia.auth.User.INFO_USER_LAST_LOGIN_DATE;
import static it.vige.rubia.feeds.FeedConstants.GLOBAL;
import static it.vige.rubia.format.render.bbcodehtml.ToHTMLConfig.FILTER_MODE_ALWAYS_PRINT;
import static it.vige.rubia.format.render.bbcodehtml.ToHTMLConfig.FILTER_MODE_NEVER_PRINT;
import static it.vige.rubia.format.render.bbcodehtml.ToHTMLConfig.OUTPUT_MODE_REMOVE;
import static it.vige.rubia.ui.Constants.DEFAULT_DATE_PATTERN;
import static it.vige.rubia.ui.JSFUtil.getContextPath;
import static it.vige.rubia.ui.JSFUtil.getDefaultLocale;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Long.parseLong;
import static java.lang.Long.valueOf;
import static java.lang.Thread.currentThread;
import static java.util.ResourceBundle.getBundle;
import static javax.faces.context.FacesContext.getCurrentInstance;
import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.User;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.auth.UserProfileModule;
import it.vige.rubia.format.render.bbcodehtml.ToHTMLConfig;
import it.vige.rubia.format.render.bbcodehtml.ToHTMLRenderer;
import it.vige.rubia.model.Message;
import it.vige.rubia.model.Poll;
import it.vige.rubia.model.PollOption;
import it.vige.rubia.model.Poster;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * 
 */
public class PortalUtil {
	/**
	 * This Map contains sorted pairs of view name and view id.
	 */
	public static final Map<String, String> VIEW_NAME_TO_ID;

	/**
	 * This Map contains sorted pairs of view id and view name.
	 */
	public static final Map<String, String> VIEW_ID_TO_NAME;

	public static final String VIEW = "v";

	static {

		SortedMap<String, String> TEMP_VIEW_ID_TO_NAME = new TreeMap<String, String>();
		SortedMap<String, String> TEMP_VIEW_NAME_TO_ID = new TreeMap<String, String>();

		// Root views
		TEMP_VIEW_ID_TO_NAME.put("i", "/views/index.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/index.xhtml", "i");

		TEMP_VIEW_ID_TO_NAME.put("j", "/views/jumpbox.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/jumpbox.xhtml", "j");

		TEMP_VIEW_ID_TO_NAME.put("m", "/views/portal_index.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/portal_index.xhtml", "m");

		// admin views
		TEMP_VIEW_ID_TO_NAME.put("a", "/views/admin/index.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/admin/index.xhtml", "a");

		TEMP_VIEW_ID_TO_NAME.put("h", "/views/admin/editCategory.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/admin/editCategory.xhtml", "h");

		TEMP_VIEW_ID_TO_NAME.put("k", "/views/admin/editForum.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/admin/editForum.xhtml", "k");

		TEMP_VIEW_ID_TO_NAME.put("l", "/views/admin/deleteCategory.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/admin/deleteCategory.xhtml", "l");

		TEMP_VIEW_ID_TO_NAME.put("d", "/views/admin/deleteForum.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/admin/deleteForum.xhtml", "d");

		// category views
		TEMP_VIEW_ID_TO_NAME.put("c", "/views/category/viewcategory_body.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/category/viewcategory_body.xhtml", "c");

		// common views
		TEMP_VIEW_ID_TO_NAME.put("b", "/views/common/common.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/common/common.xhtml", "b");

		TEMP_VIEW_ID_TO_NAME.put("n", "/views/common/common_noMenu.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/common/common_noMenu.xhtml", "n");

		// errors views
		TEMP_VIEW_ID_TO_NAME.put("e", "/views/errors/error_body.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/errors/error_body.xhtml", "e");

		// forums views
		TEMP_VIEW_ID_TO_NAME.put("f", "/views/forums/viewforum_body.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/forums/viewforum_body.xhtml", "f");

		// moderator views
		TEMP_VIEW_ID_TO_NAME.put("m", "/views/moderator/modcp_body.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/moderator/modcp_body.xhtml", "m");

		TEMP_VIEW_ID_TO_NAME.put("v", "/views/moderator/delete_topic.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/moderator/delete_topic.xhtml", "v");

		TEMP_VIEW_ID_TO_NAME.put("vs", "/views/moderator/delete_topics.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/moderator/delete_topics.xhtml", "vs");

		TEMP_VIEW_ID_TO_NAME.put("q", "/views/moderator/modcp_move.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/moderator/modcp_move.xhtml", "q");

		TEMP_VIEW_ID_TO_NAME.put("o", "/views/moderator/modcp_split.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/moderator/modcp_split.xhtml", "o");

		// preferences views
		TEMP_VIEW_ID_TO_NAME.put("r", "/views/pref/index.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/pref/index.xhtml", "r");

		// profile views
		TEMP_VIEW_ID_TO_NAME.put("u", "/views/profile/viewprofile_body.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/profile/viewprofile_body.xhtml", "u");

		TEMP_VIEW_ID_TO_NAME.put("ue", "/views/profile/usermessage_body.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/profile/usermessage_body.xhtml", "ue");

		// summary views
		TEMP_VIEW_ID_TO_NAME.put("s", "/views/summary/viewsummary_body.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/summary/viewsummary_body.xhtml", "s");

		// topic views
		TEMP_VIEW_ID_TO_NAME.put("t", "/views/topics/viewtopic_body.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/topics/viewtopic_body.xhtml", "t");

		TEMP_VIEW_ID_TO_NAME.put("p", "/views/topics/posting_new_body.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/topics/posting_new_body.xhtml", "p");

		TEMP_VIEW_ID_TO_NAME.put("g", "/views/topics/posting_edit_body.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/topics/posting_edit_body.xhtml", "g");

		TEMP_VIEW_ID_TO_NAME.put("z", "/views/topics/posting_reply_body.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/topics/posting_reply_body.xhtml", "z");

		TEMP_VIEW_ID_TO_NAME.put("y", "/views/topics/delete_poll.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/topics/delete_poll.xhtml", "y");

		TEMP_VIEW_ID_TO_NAME.put("x", "/views/topics/delete_post.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/topics/delete_post.xhtml", "x");

		TEMP_VIEW_ID_TO_NAME.put("pa", "/views/topics/attachmentsview.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/topics/attachmentsview.xhtml", "pa");

		TEMP_VIEW_ID_TO_NAME.put("pe", "/views/topics/posting_edit_preview.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/topics/posting_edit_preview.xhtml", "pe");

		TEMP_VIEW_ID_TO_NAME.put("pi", "/views/topics/posting_new_preview.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/topics/posting_new_preview.xhtml", "pi");

		TEMP_VIEW_ID_TO_NAME.put("pr", "/views/topics/posting_reply_preview.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/topics/posting_reply_preview.xhtml", "pr");

		TEMP_VIEW_ID_TO_NAME.put("pc", "/views/topics/viewtopic_poll_ballot.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/topics/viewtopic_poll_ballot.xhtml", "pc");

		TEMP_VIEW_ID_TO_NAME.put("pk", "/views/topics/viewtopic_poll_result.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/topics/viewtopic_poll_result.xhtml", "pk");

		// watches views
		TEMP_VIEW_ID_TO_NAME.put("w", "/views/watches/forumWatch.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/watches/forumWatch.xhtml", "w");
		TEMP_VIEW_ID_TO_NAME.put("wt", "/views/watches/topicWatch.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/watches/topicWatch.xhtml", "wt");

		// MyForums views
		TEMP_VIEW_ID_TO_NAME.put("my", "/views/myforums/myforums_main.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/myforums/myforums_main.xhtml", "my");

		TEMP_VIEW_ID_TO_NAME.put("ma", "/views/myforums/myforums_viewall.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/myforums/myforums_viewall.xhtml", "ma");

		TEMP_VIEW_ID_TO_NAME.put("me", "/views/myforums/myforums_editforums.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/myforums/myforums_editforums.xhtml", "me");

		// Search views
		TEMP_VIEW_ID_TO_NAME.put("se", "/views/search/viewsearch_body.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/search/viewsearch_body.xhtml", "se");

		TEMP_VIEW_ID_TO_NAME.put("sr", "/views/search/viewsearch_results.xhtml");
		TEMP_VIEW_NAME_TO_ID.put("/views/search/viewsearch_results.xhtml", "sr");

		// TEMP_VIEW_ID_TO_NAME.put("sr","/views/search/myforums_viewall.xhtml");
		// TEMP_VIEW_NAME_TO_ID.put("/views/search/myforums_viewall.xhtml","sa");

		VIEW_ID_TO_NAME = Collections.unmodifiableSortedMap(TEMP_VIEW_ID_TO_NAME);
		VIEW_NAME_TO_ID = Collections.unmodifiableSortedMap(TEMP_VIEW_NAME_TO_ID);

	}

	/**
	 * Get a <code>SimpleDateFormat</code> object from the session. The object
	 * is stored in the session because it is expensive to create and we want to
	 * reuse it as much as we can. Also it is configured with the date format
	 * taken from the preference of the user if it exists.
	 * 
	 * @return the format object
	 */
	public static SimpleDateFormat getSDF() {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(DEFAULT_DATE_PATTERN);
		return sdf;
	}

	/**
	 * @return true if the application is inside a portal
	 *
	 */
	public static boolean isRunningInPortal() {
		boolean isRunningInPortal = false;
		return isRunningInPortal;
	}

	/**
	 * @param userModule
	 *            the user module where search the user
	 * @return the current user
	 * @throws Exception
	 *             the error exception
	 */
	public static User getUser(UserModule userModule) throws Exception {
		User user = null;
		String userName = getCurrentInstance().getExternalContext().getRemoteUser();
		if (userName != null && userName.trim().length() > 0) {
			try {
				user = userModule.findUserByUserName(userName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (userName == null)
			user = getUserNA();
		return user;
	}

	/**
	 * This method translates standard view file path into short view id
	 * parameter.
	 * 
	 * @param name
	 *            the name to set
	 * 
	 * @return the related id
	 */
	public static String getIdForName(String name) {
		if (name == null) {
			return null;
		}
		return (String) VIEW_NAME_TO_ID.get(name);
	}

	public static User getUserNA() {
		return new User() {

			private String id;
			private String userName;

			{
				this.userName = GUEST_USER;
				this.id = GUEST_USER;
			}

			public String getUserName() {
				return userName;
			}

			@Override
			public void setUserName(String userName) {
				this.userName = userName;
			}

			@Override
			public String getId() {
				return id;
			}

			@Override
			public void setId(String id) {
				this.id = id;
			}
		};
	}

	/**
	 * Creates feed link.
	 * 
	 * @param type
	 *            RSS/Atom. See FeedConstants
	 * @param what
	 *            Kind of the link. See available kinds in FeedConstants
	 * @param id
	 *            Id - for kind FeedCostants.GLOBAL is ignored
	 * @return String with proper address
	 */
	public static String createFeedLink(String type, String what, Integer id) {

		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		String url = ctx.getRequestContextPath() + "/feeds/" + type + "/" + what
				+ (GLOBAL.equals(what) ? "" : "/" + id.toString());
		String urlParam = getContextPath();
		String urlType = "s";

		url += "?url=" + urlParam + "&urlType=" + urlType;

		return url;
	}

	public static Date getUserLastLoginDate(UserModule userModule, UserProfileModule userProfileModule) {
		try {
			User user = getUser(userModule);
			if (user == null) {
				return null;
			}
			Object property = userProfileModule.getProperty(user, INFO_USER_LAST_LOGIN_DATE);
			if (property != null) {
				long time = 0;
				try {
					time = valueOf(property.toString());
				} catch (NumberFormatException ex) {

				}
				Date date;
				if (time == 0) {
					DateFormat sdfForLastLoginDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
							getDefaultLocale());
					date = sdfForLastLoginDate.parse(property.toString());
				} else
					date = new Date(time);
				return date;
			}
		} catch (Exception e) {
			JSFUtil.handleException(e);
		}
		return null;
	}

	/**
	 * @param userModule
	 *            the user module of the application
	 * @param forumsModule
	 *            the forums module of the application
	 *
	 * @return the current poster
	 * @throws Exception
	 *             the error exception
	 */
	public static Poster getPoster(UserModule userModule, ForumsModule forumsModule) throws Exception {
		Poster poster = null;

		if (!userModule.isGuest()) {
			User user = getUser(userModule);

			Object userId = user.getId();
			poster = forumsModule.findPosterByUserId(userId.toString());

			if (poster == null) {
				poster = new Poster(userId.toString());
			}
		} else {
			poster = getGuestPoster(userModule, forumsModule);
		}

		return poster;
	}

	public static Poster getGuestPoster(UserModule userModule, ForumsModule forumsModule) throws Exception {
		Poster poster = null;
		User user = null;

		String guestUserName = forumsModule.getGuestUserName();
		try {
			user = userModule.findUserByUserName(guestUserName);
		} catch (Exception e) {
		}

		Long userId = parseLong(user.getId());
		poster = forumsModule.findPosterByUserId(String.valueOf(userId));

		if (poster == null) {
			poster = new Poster(userId.toString());
		}

		return poster;
	}

	/**
	 * 
	 * @author sshah
	 * 
	 * @return the created message
	 */
	public static Message createMessage() {
		Message message = new Message();
		return message;
	}

	/**
	 * 
	 * @return the created poll
	 */
	public static Poll createPoll() {
		Poll poll = new Poll();

		poll.setTitle("");
		poll.setCreationDate(new Date());

		return poll;
	}

	/**
	 * @param poll
	 *            the poll where create the option
	 * @return the created poll option
	 */
	public static PollOption createPollOption(Poll poll) {
		PollOption pollOption = new PollOption(poll);
		return pollOption;
	}

	/**
	 * @param req
	 *            the request to set
	 * @return the related renderer
	 */
	private static ToHTMLRenderer getToHTMLRenderer(Object req) {
		ToHTMLRenderer renderer = null;
		// TODO: GETTING RENDERER FROM APPLICATION SCOPE ATTRIBUTE
		if (renderer == null) {

			// Getting ResourceBundle with current Locale
			FacesContext ctx = getCurrentInstance();
			UIViewRoot uiRoot = ctx.getViewRoot();
			Locale locale = uiRoot.getLocale();
			ClassLoader ldr = currentThread().getContextClassLoader();
			ResourceBundle bundle = getBundle("ResourceJSF", locale, ldr);

			// Create the HTMLRenderer for BBCode
			ToHTMLConfig config = new ToHTMLConfig();
			renderer = new ToHTMLRenderer(config, bundle);
		}
		return renderer;
	}

	/**
	 * Method used for parsing bbcode and return properly formated text of
	 * message.
	 * 
	 * @param text
	 *            the text to format
	 * @param allowHTML
	 *            true if the text supports html *
	 * @return the formatted message
	 */
	public static String formatMessage(String text, boolean allowHTML) {

		try {
			Object req = getCurrentInstance().getExternalContext().getRequest();

			if (allowHTML) {
				getToHTMLRenderer(req).getConfig().setFilterMode(FILTER_MODE_ALWAYS_PRINT);
				getToHTMLRenderer(req).getConfig().setOuputMode(OUTPUT_MODE_REMOVE);
				getToHTMLRenderer(req).getConfig().setMaxTextWidth(MAX_VALUE);
			} else {
				getToHTMLRenderer(req).getConfig().setFilterMode(FILTER_MODE_NEVER_PRINT);
				getToHTMLRenderer(req).getConfig().setOuputMode(OUTPUT_MODE_REMOVE);
				getToHTMLRenderer(req).getConfig().setMaxTextWidth(MAX_VALUE);
			}
			return formatTitle(req, text);
		} catch (Exception e) {
			// Now if something goes wrong it just returns message with bbcode.
			return text;
		}

	}

	/**
	 * @param req
	 *            the request where take the renderer
	 * @param text
	 *            the text to format
	 * @return the formatted message
	 */
	public static String formatTitle(Object req, String text) {

		StringWriter stringWriter = new StringWriter();
		getToHTMLRenderer(req).render(text.toCharArray(), 0, text.length());
		getToHTMLRenderer(req).getConfig().setMaxTextWidth(MAX_VALUE);
		return stringWriter.toString();

	}

	/**
	 * This method translates short id view parameter into standard view file
	 * path.
	 * 
	 * @param id
	 *            the id to set
	 * @return the name related to the id
	 */
	public static String getNameForId(String id) {
		if (id == null) {
			return null;
		}
		return VIEW_ID_TO_NAME.get(id);
	}

	/**
	 * @param forumsModule
	 *            the current forum module
	 * @param userModule
	 *            the current user module
	 * @return the current poster
	 * @throws Exception
	 *             the error exception
	 */
	public static Poster getPoster(ForumsModule forumsModule, UserModule userModule) throws Exception {
		Poster poster = null;

		User user = getUser(userModule);

		String userId = user.getId();
		poster = forumsModule.findPosterByUserId(userId);

		if (poster == null) {
			poster = new Poster(userId);
		}

		return poster;
	}

	/**
	 * @param poll
	 *            the poll where search the option
	 * @param option
	 *            the option where search the value
	 * @param multiplicator
	 *            the multiplicator to calculate the new percent
	 * @return the formatted percent value
	 * 
	 */
	public static String getVotePercent(Poll poll, PollOption option, int multiplicator) {
		float votePercent = 0;

		float votesSum = poll.getVotesSum();
		if (votesSum > 0) {
			votePercent = (option.getVotes() / votesSum);
		}

		if (multiplicator == 0) {
			String result = "";

			DecimalFormat decimalFormat = new DecimalFormat("##.##%");
			result = decimalFormat.format(votePercent);
			return result;
		}
		return votePercent * multiplicator + "";
	}

}
