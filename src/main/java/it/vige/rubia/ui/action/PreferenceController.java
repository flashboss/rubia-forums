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
package it.vige.rubia.ui.action;

import static it.vige.rubia.ui.view.SummaryMode.BLOCK_TOPICS_MODE_LATEST_POSTS;
import it.vige.rubia.ui.BaseController;
import it.vige.rubia.ui.view.SummaryMode;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

/*
 * Created on May 24, 2006
 *
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 */
@Named("prefController")
@SessionScoped
public class PreferenceController extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3891010378190343197L;

	/**
	 * data associated with "notify on reply" the default value of this is setup
	 * using the managed-property value in the faces config file
	 */
	private boolean notifyOnReply = true;

	/**
	 * data associated with "always allow html" the default value of this is
	 * setup using the managed-property value in the faces config file
	 */
	private boolean alwaysAllowHtml = true;

	/**
	 * data associated with "post order" the default value of this is setup
	 * using the managed-property value in the faces config file
	 */
	private String postOrder = "ascending";

	/**
	 * data associated with "date format" the default value of this is setup
	 * using the managed-property value in the faces config file
	 */
	private String dateFormat = "EEE MMM d, yyyy HH:mm aaa";

	/**
	 * data associated with "always add signature" the default value of this is
	 * setup using the managed-property value in the faces config file
	 */
	private boolean alwaysAddSignature = false;

	/**
	 * data associated with "signature"
	 */
	private String signature;

	/**
	 * summary related preferences
	 */
	private SummaryMode summaryMode = BLOCK_TOPICS_MODE_LATEST_POSTS;
	private int summaryTopicLimit = 6;
	private int summaryTopicDays = 20;
	private int summaryTopicReplies = 15;

	private int forumInstanceId = 1;

	/**
	 * topic related preferences
	 */
	private int postsPerTopic = 15;

	/**
	 * forum related preferences
	 * 
	 */
	private int topicsPerForum = 10;

	// ------accessors-------------------------------------------------------------------------------------------------------------------------------
	/**
     * 
     */
	public boolean isNotifyOnReply() {
		return this.notifyOnReply;
	}

	/**
     * 
     *
     */
	public void setNotifyOnReply(boolean notifyOnReply) {
		this.notifyOnReply = notifyOnReply;
	}

	/**
	 * @return Returns the alwaysAllowHtml.
	 */
	public boolean isAlwaysAllowHtml() {
		return alwaysAllowHtml;
	}

	/**
	 * @param alwaysAllowHtml
	 *            The alwaysAllowHtml to set.
	 */
	public void setAlwaysAllowHtml(boolean alwaysAllowHtml) {
		this.alwaysAllowHtml = alwaysAllowHtml;
	}

	/**
	 * @return Returns the postOrder.
	 */
	public String getPostOrder() {
		return postOrder;
	}

	/**
	 * @param postOrder
	 *            The postOrder to set.
	 */
	public void setPostOrder(String postOrder) {
		this.postOrder = postOrder;
	}

	/**
	 * @return Returns the dateFormat.
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat
	 *            The dateFormat to set.
	 */
	public void setDateFormat(String dateFormat) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
			simpleDateFormat.format(new Date());
		} catch (Exception e) {
			return; // don't modify with the submitted dateFormat
		}

		// if I get here, the input date format is fine
		this.dateFormat = dateFormat;
	}

	/**
	 * @return Returns the alwaysAddSignature.
	 */
	public boolean isAlwaysAddSignature() {
		return alwaysAddSignature;
	}

	/**
	 * @param alwaysAddSignature
	 *            The alwaysAddSignature to set.
	 */
	public void setAlwaysAddSignature(boolean alwaysAddSignature) {
		this.alwaysAddSignature = alwaysAddSignature;
	}

	/**
	 * @return Returns the signature.
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature
	 *            The signature to set.
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
     * 
     *
     */
	public SummaryMode getSummaryMode() {
		return summaryMode;
	}

	/**
	 * 
	 * @param summaryMode
	 */
	public void setSummaryMode(SummaryMode summaryMode) {
		this.summaryMode = summaryMode;
	}

	/**
     * 
     *
     */
	public int getSummaryTopicLimit() {
		return summaryTopicLimit;
	}

	/**
	 * 
	 * @param summaryTopicLimit
	 */
	public void setSummaryTopicLimit(int summaryTopicLimit) {
		this.summaryTopicLimit = summaryTopicLimit;
	}

	/**
     * 
     *
     */
	public int getSummaryTopicDays() {
		return this.summaryTopicDays;
	}

	/**
	 * 
	 * @param summaryTopicDays
	 */
	public void setSummaryTopicDays(int summaryTopicDays) {
		this.summaryTopicDays = summaryTopicDays;
	}

	/**
     * 
     *
     */
	public int getSummaryTopicReplies() {
		return summaryTopicReplies;
	}

	/**
	 * 
	 * @param summaryTopicReplies
	 */
	public void setSummaryTopicReplies(int summaryTopicReplies) {
		this.summaryTopicReplies = summaryTopicReplies;
	}

	/**
	 * @return Returns the postsPerTopic.
	 */
	public int getPostsPerTopic() {
		return postsPerTopic;
	}

	/**
	 * @param postsPerTopic
	 *            The postsPerTopic to set.
	 */
	public void setPostsPerTopic(int postsPerTopic) {
		this.postsPerTopic = postsPerTopic;
	}

	/**
	 * @return Returns the topicsPerForum.
	 */
	public int getTopicsPerForum() {
		return topicsPerForum;
	}

	/**
	 * @param topicsPerForum
	 *            The topicsPerForum to set.
	 */
	public void setTopicsPerForum(int topicsPerForum) {
		this.topicsPerForum = topicsPerForum;
	}

	public int getForumInstanceId() {
		return forumInstanceId;
	}

	public void setForumInstanceId(int forumInstanceId) {
		this.forumInstanceId = forumInstanceId;
	}

	// -------cleanup-------------------------------------------------------------------------------------------------------------------------------
	public void cleanup() {
	}

	public String execute() {
		return SUCCESS;
	}

	public SelectItem[] getSummaryModeValues() {
		SelectItem[] items = new SelectItem[SummaryMode.values().length];
		int i = 0;
		for (SummaryMode g : SummaryMode.values()) {
			items[i++] = new SelectItem(g, g.toString());
		}
		return items;
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------
}
