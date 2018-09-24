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
package it.vige.rubia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
public interface Constants {
	// ---navigation state constants...these values map to actual xhtml facelet
	// files. The mapping is specified in the forums-config.xml
	// file--------------------------------------------------------------------------------------------------
	String ERROR = "error";
	String START = "start";
	String CANCEL = "cancel";
	String SUCCESS = "success";
	String START_REPLY = "startReply";
	String START_NEW_TOPIC = "startNewTopic";
	String START_EDIT_POST = "startEditPost";
	String TOPIC_DELETED = "topicDeleted";
	String FEEDBACK = "feedback";
	String EDIT_CATEGORY = "editCategory";
	String EDIT_FORUM = "editForum";
	String ADD_CATEGORY = "addCategory";
	String ADD_FORUM = "addForum";
	String EDIT_WATCH = "editWatch";
	String DELETE_CATEGORY = "deleteCategory";
	String DELETE_FORUM = "deleteForum";
	// ---------parameter
	// constants--------------------------------------------------------------------------------------------------
	String p_categoryId = "c";
	String p_forumId = "f";
	String p_topicId = "t";
	String p_postId = "p";
	String p_userId = "uid";
	String p_option = "o";
	String p_forum_to_id = "forum_to_id";
	String p_poll_title = "POLL_TITLE";
	String p_poll_delete = "POLL_DELETE";
	String p_attachment = "ATTACHMENT";
	String p_vote = "vote";
	String p_results = "results";
	String p_watchId = "w";
	String p_viewId = "v";
	String p_notified_post_id = "notified_post_id";
	String p_notified_watch_type = "notified_watch_type";
	String p_page = "page";
	// other
	// constants-------------------------------------------------------------------------------------------------------------------------------
	String QUOTE = "blockquote";
	String NOTIFY_REPLY_KEY = "notifyreply";
	String ALLOW_HTML_KEY = "allowhtml";
	String SIG_KEY = "signature";
	String TOPIC_LOCKED_ERR_KEY = "topiclockederr";
	String BUNDLE_NAME = "ResourceJSF";
	// ---------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * When a notification message is sent, send a link to the message.
	 */
	int WATCH_MODE_LINKED = 0;

	/**
	 * When a notification message is sent, send the message content.
	 */
	int WATCH_MODE_EMBEDED = 1;

	/**
	 * DOCUMENT_ME
	 */
	int FORUM_LOCKED = 1;

	/**
	 * DOCUMENT_ME
	 */
	int FORUM_UNLOCKED = 0;

	/**
	 * DOCUMENT_ME
	 */
	int TOPIC_UNLOCKED = 0;

	/**
	 * DOCUMENT_ME
	 */
	int TOPIC_LOCKED = 1;

	/**
	 * DOCUMENT_ME
	 */
	int POST_GLOBAL_ANNONCE = 3;

	/**
	 * DOCUMENT_ME
	 */
	int TOPIC_MOVED = 2;
	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_SMALL_HEADER = "small_header";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_TOPIC_NOTIFY = "search_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_SEARCH_BODY = "search_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_MESSAGE_BODY = "message_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_INDEX_BODY = "index_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_VIEWFORUM_BODY = "viewforum_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_VIEWTOPIC_BODY = "viewtopic_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_VIEWTOPIC_ATTACH_BODY = "viewtopic_attach_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_VIEWTOPIC_POLL_RESULT = "viewtopic_poll_result";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_VIEWTOPIC_POLL_BALLOT = "viewtopic_poll_ballot";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_JUMP_BOX = "jumpbox";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_POSTING_BODY = "posting_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_POSTING_POLL_BODY = "posting_poll_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_POSTING_ATTACH_BODY = "posting_attach_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_ADD_ATTACHMENT_BODY = "add_attachment_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_POSTED_ATTACHMENTS_BODY = "posted_attachments_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_POSTING_TOPIC_REVIEW = "posting_topic_review";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_POSTING_PREVIEW = "posting_preview";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_MODCP_BODY = "modcp_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_MODCP_MOVE = "modcp_move";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CONFIRM_BODY = "confirm_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_ERROR_BODY = "error_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_SEARCH_RESULTS_POSTS = "search_results_posts";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_SEARCH_RESULTS_TOPICS = "search_results_topics";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_WATCHED_TOPICS_BODY = "watched_topics_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_WATCHED_FORUMS_BODY = "watched_forums_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_ADMIN_AUTH_FORUM_BODY = "admin/auth_forum_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_ADMIN_AUTH_SELECT_BODY = "admin/auth_select_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_ADMIN_FORUM_ADMIN_BODY = "admin/forum_admin_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_ADMIN_FORUM_EDIT_BODY = "admin/forum_edit_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_ADMIN_CATEGORY_EDIT_BODY = "admin/category_edit_body";

	/**
	 * DOCUMENT_ME
	 */
	String THEMENAME = "default_graphics";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_ADMIN_FORUM_DELETE_BODY = "admin/forum_delete_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_ADMIN_ATTACH_MANAGE_BODY = "admin/attach_manage_body";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_B_CLOSE = "code/b_close";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_B_OPEN = "code/b_open";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_CODE_CLOSE = "code/code_close";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_CODE_OPEN = "code/code_open";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_COLOR_CLOSE = "code/color_close";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_COLOR_OPEN = "code/color_open";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_EMAIL = "code/email";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_I_CLOSE = "code/i_close";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_I_OPEN = "code/i_open";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_IMG = "code/img";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_LISTITEM = "code/listitem";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_OLIST_CLOSE = "code/olist_close";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_OLIST_OPEN = "code/olist_open";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_QUOTE_CLOSE = "code/quote_close";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_QUOTE_OPEN = "code/quote_open";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_QUOTE_USERNAME_OPEN = "code/quote_username_open";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_SIZE_CLOSE = "code/size_close";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_SIZE_OPEN = "code/size_open";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_U_CLOSE = "code/u_close";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_U_OPEN = "code/u_open";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_ULIST_CLOSE = "code/ulist_close";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_ULIST_OPEN = "code/ulist_open";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_CODE_URL = "code/url";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_THEME_BLOCK_TOPICS = "block/topics";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_MAIL_TOPIC_NOTIFY = "topic_notify";

	/**
	 * DOCUMENT_ME
	 */
	String TPL_MAIL_FORUM_NOTIFY = "forum_notify";

	/**
	 * DOCUMENT_ME
	 */
	String[] SORT_BY_TYPES = { "${bb.Sort_Time}", "${bb.Sort_Post_Subject}",
			"${bb.Sort_Author}", "${bb.Sort_Forum}" };

	/**
	 * DOCUMENT_ME
	 */
	int[] PREVIOUS_DAYS = { 0, 1, 7, 14, 30, 90, 180, 364 };

	/**
	 * DOCUMENT_ME
	 */
	String[] PREVIOUS_DAYS_TEXT_TOPICS = { "${bb.All_Topics}", "${bb.1_Day}",
			"${bb.7_Days}", "${bb.2_Weeks}", "${bb.1_Month}", "${bb.3_Months}",
			"${bb.6_Months}", "${bb.1_Year}" };

	/**
	 * DOCUMENT_ME
	 */
	String[] PREVIOUS_DAYS_TEXT_POSTS = { "${bb.All_Posts}", "${bb.1_Day}",
			"${bb.7_Days}", "${bb.2_Weeks}", "${bb.1_Month}", "${bb.3_Months}",
			"${bb.6_Months}", "${bb.1_Year}" };

	/**
	 * DOCUMENT_ME
	 */
	String[] FORUM_AUTH_FORM_FIELD_NAMES = { "auth_view", "auth_read",
			"auth_post", "auth_reply", "auth_edit", "auth_delete",
			"auth_sticky", "auth_announce", "auth_vote", "auth_pollcreate",
			"auth_attachment" };

	/**
	 * DOCUMENT_ME
	 */
	String[] FORUM_AUTH_FORM_FIELD_LABELS = { "${bb.View}", "${bb.Read}",
			"${bb.Post}", "${bb.Reply}", "${bb.Edit}", "${bb.Delete}",
			"${bb.Sticky}", "${bb.Announce}", "${bb.Vote}", "${bb.Pollcreate}",
			"${bb.UploadedFile}" };

	/**
	 * DOCUMENT_ME
	 */
	String[] FORUM_AUTH_LEVEL_LABELS = { "ALL", "REG", "PRIVATE", "MOD",
			"ADMIN" };

	/**
	 * DOCUMENT_ME
	 */
	int GENERAL_MESSAGE = 200;

	/**
	 * DOCUMENT_ME
	 */
	int GENERAL_ERROR = 202;

	/**
	 * DOCUMENT_ME
	 */
	int CRITICAL_MESSAGE = 203;

	/**
	 * DOCUMENT_ME
	 */
	int CRITICAL_ERROR = 204;

	/**
	 * DOCUMENT_ME
	 */
	String[] AUTH_TYPE_TO_MESSAGE = { null, null, "Sorry_auth_read_",
			"Sorry_auth_post_", "Sorry_auth_reply_", "Sorry_auth_edit_",
			"Sorry_auth_delete_", "Sorry_auth_announce_", "Sorry_auth_sticky_",
			null, null, "Sorry_auth_vote_", null, };

	// modes

	/**
	 * DOCUMENT_ME
	 */
	int PMODE_NO_MODE = -1;

	/**
	 * DOCUMENT_ME
	 */
	int PMODE_VOTE = 0;

	/**
	 * DOCUMENT_ME
	 */
	int PMODE_REPLY = 1;

	/**
	 * DOCUMENT_ME
	 */
	int PMODE_QUOTE = 2;

	/**
	 * DOCUMENT_ME
	 */
	int PMODE_EDIT_POST = 3;

	/**
	 * DOCUMENT_ME
	 */
	int PMODE_DELETE = 4;

	/**
	 * DOCUMENT_ME
	 */
	int PMODE_POLL_DELETE = 5;

	/**
	 * DOCUMENT_ME
	 */
	int PMODE_NEW_TOPIC = 6;

	/**
	 * DOCUMENT_ME
	 */
	int PMODE_REPOST = 7;

	/**
	 * DOCUMENT_ME
	 */
	int PMODE_SMILIES = 8;

	/**
	 * DOCUMENT_ME
	 */
	int PMODE_TOPIC_REVIEW = 9;

	/**
	 * DOCUMENT_ME
	 */
	int PMASK_VOTE = 0x0000001;

	/**
	 * DOCUMENT_ME
	 */
	int PMASK_REPLY = 0x0000002;

	/**
	 * DOCUMENT_ME
	 */
	int PMASK_QUOTE = 0x0000004;

	/**
	 * DOCUMENT_ME
	 */
	int PMASK_EDIT_POST = 0x0000008;

	/**
	 * DOCUMENT_ME
	 */
	int PMASK_DELETE = 0x0000010;

	/**
	 * DOCUMENT_ME
	 */
	int PMASK_POLL_DELETE = 0x0000020;

	/**
	 * DOCUMENT_ME
	 */
	int PMASK_NEW_TOPIC = 0x0000040;

	/**
	 * DOCUMENT_ME
	 */
	int PMASK_SMILIES = 0x0000080;

	/**
	 * DOCUMENT_ME
	 */
	int PMASK_TOPIC_REVIEW = 0x0000100;

	/**
	 * DOCUMENT_ME
	 */
	int[] PMASKS = { PMASK_VOTE, PMASK_REPLY, PMASK_QUOTE, PMASK_EDIT_POST,
			PMASK_DELETE, PMASK_POLL_DELETE, PMASK_NEW_TOPIC, PMASK_SMILIES,
			PMASK_TOPIC_REVIEW };

	/**
	 * DOCUMENT_ME
	 */
	int MMODE_NO_MODE = -1;

	/**
	 * DOCUMENT_ME
	 */
	int MMODE_DELETE = 0;

	/**
	 * DOCUMENT_ME
	 */
	int MMODE_MOVE = 1;

	/**
	 * DOCUMENT_ME
	 */
	int MMODE_LOCK = 2;

	/**
	 * DOCUMENT_ME
	 */
	int MMODE_UNLOCK = 3;

	/**
	 * DOCUMENT_ME
	 */
	int MMODE_SPLIT = 4;

	/**
	 * DOCUMENT_ME
	 */
	int MMODE_IP = 5;

	/**
	 * DOCUMENT_ME
	 */
	String[] AUTH_PRESETS_NAMES = { "${bb.Public}", "${bb.Registered}",
			"${bb.Registered}[${bb.Hidden}]", "${bb.Private}",
			"${bb.Private}[${bb.Hidden}]", "${bb.Moderators}",
			"${bb.Moderators}[${bb.Hidden}]" };

	/**
	 * DOCUMENT_ME
	 * 
	 * @author $author$
	 * @version $Revision: 1018 $
	 */
	class ModeDecoder {
		private static final String[] P_TO_HTML = new String[10];

		static {
			P_TO_HTML[PMODE_VOTE] = "vote";
			P_TO_HTML[PMODE_REPLY] = "reply";
			P_TO_HTML[PMODE_QUOTE] = "reply";
			P_TO_HTML[PMODE_EDIT_POST] = "editpost";
			P_TO_HTML[PMODE_DELETE] = "delete";
			P_TO_HTML[PMODE_POLL_DELETE] = "poll_delete";
			P_TO_HTML[PMODE_NEW_TOPIC] = "newtopic";
			P_TO_HTML[PMODE_REPOST] = "repost";
			P_TO_HTML[PMODE_SMILIES] = "smilies";
			P_TO_HTML[PMODE_TOPIC_REVIEW] = "topicreview";
		}

		/**
		 * DOCUMENT_ME
		 * 
		 * @param mode
		 *            DOCUMENT_ME
		 * @return DOCUMENT_ME
		 */
		String encodePosting(int mode) {
			return P_TO_HTML[mode];
		}

		/**
		 * DOCUMENT_ME
		 * 
		 * @param mode
		 *            DOCUMENT_ME
		 * @return DOCUMENT_ME
		 */
		int decodePosting(String mode) {
			if ("topicreview".equals(mode)) {
				return PMODE_TOPIC_REVIEW;
			}

			if ("smilies".equals(mode)) {
				return PMODE_SMILIES;
			}

			if ("reply".equals(mode)) {
				return PMODE_REPLY;
			}

			if ("quote".equals(mode)) {
				return PMODE_QUOTE;
			}

			if ("editpost".equals(mode)) {
				return PMODE_EDIT_POST;
			}

			if ("delete".equals(mode)) {
				return PMODE_DELETE;
			}

			if ("poll_delete".equals(mode)) {
				return PMODE_POLL_DELETE;
			}

			if ("vote".equals(mode)) {
				return PMODE_VOTE;
			}

			if ("newtopic".equals(mode)) {
				return PMODE_NEW_TOPIC;
			}

			if ("repost".equals(mode)) {
				return PMODE_REPOST;
			}

			return PMODE_NO_MODE;
		}
	}

	/**
	 * DOCUMENT_ME
	 */
	int TOPIC_WATCH_UN_NOTIFIED = 0;

	/**
	 * DOCUMENT_ME
	 */
	int TOPIC_WATCH_NOTIFIED = 1;

	/**
	 * DOCUMENT_ME
	 */
	String POST_CAT_URL = "c";

	/**
	 * DOCUMENT_ME
	 */
	String POST_FORUM_URL = "f";

	/**
	 * DOCUMENT_ME
	 */
	String POST_TOPIC_URL = "t";

	/**
	 * DOCUMENT_ME
	 */
	String POST_POST_URL = "p";

	/**
	 * DOCUMENT_ME
	 */
	String POST_USERS_URL = "u";

	/**
	 * DOCUMENT_ME
	 */
	String POST_ROLES_URL = "g";

	/**
	 * Constant saying that watched don't want to be notified by e-mail.
	 */
	int WATCH_MODE_NONE = 2;

	/**
	 * DOCUMENT_ME
	 */
	String USER_SIGNATURE_PROPERTY = "portal.user.forums.signature";

	/**
	 * DOCUMENT_ME
	 */
	String USER_SIGNATURE_ADD_PROPERTY = "portal.user.forums.addsignature";

	String DEFAULT_DATE_PATTERN = "EEE MMM d, yyyy HH:mm aaa";

	String DISK_PERSISTED_ATTACHEMENTS = "EEE MMM d, yyyy HH:mm aaa";

	String POSTING_POSTBACK_FLAG = "posting_postback_flag";

	String FILE_SESSION_INDEX = "portal.user.forums.file_session_index";

	String ATTACHMENT_TOKENS_MANAGER = "org.gatein.forums.helper.AttachmentTokenManager";

	String RE = "Re: ";

	String BY = " by ";

	String MAIN_PAGE = "Rubia Forums";

	DateFormat restDateFormat = new SimpleDateFormat("d-M-YY-h-m-a");

}
