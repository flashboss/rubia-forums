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
package org.vige.rubia.theme;

import static org.vige.rubia.ui.Constants.POST_ANNOUNCE;
import static org.vige.rubia.ui.Constants.POST_STICKY;
import static org.vige.rubia.ui.Constants.TOPIC_LOCKED;

import org.vige.rubia.properties.XProperties;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:julien@jboss.org">Thomas Heute</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
public class ForumsTheme {
	/**
	 * DOCUMENT_ME
	 * 
	 * @param type
	 *            DOCUMENT_ME
	 * @param status
	 *            DOCUMENT_ME
	 * @param hot
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 */
	public FolderType getFolderType(int type, int status, boolean hot) {
		switch (type) {
		case POST_ANNOUNCE:
			if (status == TOPIC_LOCKED) {
				return FOLDER_ANNOUNCE_LOCKED;
			} else {
				return FOLDER_ANNOUNCE;
			}

		case POST_STICKY:
			if (status == TOPIC_LOCKED) {
				return FOLDER_STICKY_LOCKED;
			} else {
				return FOLDER_STICKY;
			}

		default: {
			if (status == TOPIC_LOCKED) {
				return FOLDER_NORMAL_LOCKED;
			} else {
				if (hot) {
					return FOLDER_HOT;
				} else {
					return FOLDER_NORMAL;
				}
			}
		}
		}
	}

	/**
	 * DOCUMENT_ME
	 */
	public FolderType FOLDER_ANNOUNCE;

	/**
	 * DOCUMENT_ME
	 */
	public FolderType FOLDER_STICKY;

	/**
	 * DOCUMENT_ME
	 */
	public FolderType FOLDER_ANNOUNCE_LOCKED;

	/**
	 * DOCUMENT_ME
	 */
	public FolderType FOLDER_STICKY_LOCKED;

	/**
	 * DOCUMENT_ME
	 */
	public FolderType FOLDER_NORMAL_LOCKED;

	/**
	 * DOCUMENT_ME
	 */
	public FolderType FOLDER_NORMAL;

	/**
	 * DOCUMENT_ME
	 */
	public FolderType FOLDER_HOT;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceForumURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceForumNewURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceForumNewBigURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceForumLockedURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconLatestReplyURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconNewestReplyURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconGotopostURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconFeedURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceFolderURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceFolderNewURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceFolderAnnounceURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceFolderAnnounceNewURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceFolderStickyURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceFolderStickyNewURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceFolderLockedURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceFolderLockedNewURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceFolderHotURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceFolderHotNewURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourcePostLockedURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourcePollVotingBar;

	/**
	 * DOCUMENT_ME
	 */
	public String resourcePollVotingLBar;

	/**
	 * DOCUMENT_ME
	 */
	public String resourcePollVotingRBar;

	/**
	 * DOCUMENT_ME
	 */
	public String resourcePostNewURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceTopicModeDeleteURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceTopicModeDeleteDisaURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceTopicModMoveURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceTopicModLockURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceTopicModUnlockURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceTopicModSplitURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceAdminNewCategoryURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceAdminNewForumURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceAdminArrowUpURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceAdminArrowUpDisaURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceAdminArrowDownURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceAdminArrowDownDisaURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceTopicUnWatchURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceTopicWatchURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceReplyNewURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceReplyLockedURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconProfileURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconPMURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconEmailURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconWWWURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconICQURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconAIMURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconMSNMURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconSkypeURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconYIMURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconQuoteURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconSearchURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconEditURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconRepostURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconIPURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconDelpostURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconMinipostURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconMinipostNewURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconSpacerURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconSubscribeURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconUnSubscribeURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconLockURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconUnlockURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconDialogWarningURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconDialogErrorURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconDialogQuestionURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconModerateURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceIconForumsLogoURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceSpacerURL;

	/**
	 * DOCUMENT_ME
	 */
	public String resourceHeadStylesheetURL;
	private XProperties xprops;

	/**
	 * DOCUMENT_ME
	 * 
	 * @param xprops
	 *            DOCUMENT_ME
	 */
	public void setExtendedProperties(XProperties xprops) {
		this.xprops = xprops;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param name
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 */
	public String getProperty(String name) {
		return xprops.getProperty(name);
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param name
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 */
	public String getResourceURL(String name) {
		return xprops.getResourceURL(name);
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @throws Exception
	 *             DOCUMENT_ME
	 */
	public void start() throws Exception {
		resourceForumURL = xprops.getResourceURL("forum");
		resourceForumNewURL = xprops.getResourceURL("forum_new");
		resourceForumNewBigURL = xprops.getResourceURL("forum_new_big");
		resourceForumLockedURL = xprops.getResourceURL("forum_locked");
		resourceIconLatestReplyURL = xprops.getResourceURL("icon_latest_reply");
		resourceIconNewestReplyURL = xprops.getResourceURL("icon_newest_reply");
		resourceIconGotopostURL = xprops.getResourceURL("icon_gotopost");
		resourceIconFeedURL = xprops.getResourceURL("icon_feed");
		resourceFolderURL = xprops.getResourceURL("folder");
		resourceFolderNewURL = xprops.getResourceURL("folder_new");
		resourceFolderAnnounceURL = xprops.getResourceURL("folder_announce");
		resourceFolderAnnounceNewURL = xprops.getResourceURL("folder_announce_new");
		resourceFolderStickyURL = xprops.getResourceURL("folder_sticky");
		resourceFolderStickyNewURL = xprops.getResourceURL("folder_sticky_new");
		resourceFolderLockedURL = xprops.getResourceURL("folder_locked");
		resourceFolderLockedNewURL = xprops.getResourceURL("folder_locked_new");
		resourceFolderHotURL = xprops.getResourceURL("folder_hot");
		resourceFolderHotNewURL = xprops.getResourceURL("folder_hot_new");
		resourcePollVotingBar = xprops.getResourceURL("voting_graphic_0");
		resourcePollVotingLBar = xprops.getResourceURL("vote_lcap");
		resourcePollVotingRBar = xprops.getResourceURL("vote_rcap");
		resourcePostLockedURL = xprops.getResourceURL("post_locked");
		resourcePostNewURL = xprops.getResourceURL("post_new");
		resourceTopicModeDeleteURL = xprops.getResourceURL("topic_mod_delete");
		resourceTopicModeDeleteDisaURL = xprops.getResourceURL("topic_mod_delete_disa");
		resourceTopicModMoveURL = xprops.getResourceURL("topic_mod_move");
		resourceTopicModLockURL = xprops.getResourceURL("topic_mod_lock");
		resourceTopicModUnlockURL = xprops.getResourceURL("topic_mod_unlock");
		resourceTopicModSplitURL = xprops.getResourceURL("topic_mod_split");
		resourceAdminNewCategoryURL = xprops.getResourceURL("admin_newcategory");
		resourceAdminNewForumURL = xprops.getResourceURL("admin_newforum");
		resourceAdminArrowUpURL = xprops.getResourceURL("admin_arrowup");
		resourceAdminArrowUpDisaURL = xprops.getResourceURL("admin_arrowup_disa");
		resourceAdminArrowDownURL = xprops.getResourceURL("admin_arrowdown");
		resourceAdminArrowDownDisaURL = xprops.getResourceURL("admin_arrowdown_disa");
		resourceTopicUnWatchURL = xprops.getResourceURL("topic_un_watch");
		resourceTopicWatchURL = xprops.getResourceURL("topic_watch");
		resourceReplyNewURL = xprops.getResourceURL("reply_new");
		resourceReplyLockedURL = xprops.getResourceURL("reply_locked");
		resourceIconProfileURL = xprops.getResourceURL("icon_profile");
		resourceIconPMURL = xprops.getResourceURL("icon_pm");
		resourceIconEmailURL = xprops.getResourceURL("icon_email");
		resourceIconWWWURL = xprops.getResourceURL("icon_www");
		resourceIconICQURL = xprops.getResourceURL("icon_icq");
		resourceIconAIMURL = xprops.getResourceURL("icon_aim");
		resourceIconMSNMURL = xprops.getResourceURL("icon_msnm");
		resourceIconSkypeURL = xprops.getResourceURL("icon_skype");
		resourceIconYIMURL = xprops.getResourceURL("icon_yim");
		resourceIconQuoteURL = xprops.getResourceURL("icon_quote");
		resourceIconSearchURL = xprops.getResourceURL("icon_search");
		resourceIconEditURL = xprops.getResourceURL("icon_edit");
		resourceIconRepostURL = xprops.getResourceURL("icon_repost");
		resourceIconIPURL = xprops.getResourceURL("icon_ip");
		resourceIconDelpostURL = xprops.getResourceURL("icon_delpost");
		resourceIconMinipostURL = xprops.getResourceURL("icon_minipost");
		resourceIconMinipostNewURL = xprops.getResourceURL("icon_minipost_new");
		resourceIconSubscribeURL = xprops.getResourceURL("forum_subscribe");
		resourceIconUnSubscribeURL = xprops.getResourceURL("forum_unsubscribe");
		resourceIconUnlockURL = xprops.getResourceURL("unlock");
		resourceIconLockURL = xprops.getResourceURL("lock");
		resourceIconModerateURL = xprops.getResourceURL("moderate");
		resourceIconForumsLogoURL = xprops.getResourceURL("forums_logo");
		resourceIconDialogWarningURL = xprops.getResourceURL("icon_dialog_warning");
		resourceIconDialogQuestionURL = xprops.getResourceURL("icon_dialog_question");
		resourceIconDialogErrorURL = xprops.getResourceURL("icon_dialog_error");
		resourceSpacerURL = xprops.getResourceURL("spacer");
		resourceHeadStylesheetURL = xprops.getResourceURL("head_stylesheet");
		resourceIconSpacerURL = xprops.getResourceURL("spacer");

		//
		FOLDER_ANNOUNCE = new FolderType(resourceFolderAnnounceURL, resourceFolderAnnounceNewURL, "Topic_Announcement");
		FOLDER_STICKY = new FolderType(resourceFolderStickyURL, resourceFolderStickyNewURL, "Topic_Sticky");
		FOLDER_ANNOUNCE_LOCKED = new FolderType(resourceFolderLockedURL, resourceFolderLockedNewURL, "Topic_Announcement");
		FOLDER_STICKY_LOCKED = new FolderType(resourceFolderLockedURL, resourceFolderLockedNewURL, "Topic_Sticky");
		FOLDER_NORMAL_LOCKED = new FolderType(resourceFolderLockedURL, resourceFolderLockedNewURL, "");
		FOLDER_HOT = new FolderType(resourceFolderHotURL, resourceFolderHotNewURL, "");
		FOLDER_NORMAL = new FolderType(resourceFolderURL, resourceForumNewURL, "");
	}
}
