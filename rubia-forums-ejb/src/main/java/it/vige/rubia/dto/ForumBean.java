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
package it.vige.rubia.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Category of forums.
 * 
 * @author <a href="mailto:julien@jboss.org">Julien Viet </a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute </a>
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * @version $Revision: 1124 $
 */

public class ForumBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5226669076550683642L;

	/**
	 * Creates a new {@link ForumBean} object.
	 */
	public ForumBean() {
		setTopics(new ArrayList<TopicBean>());
	}

	public ForumBean(String name) {
		this();
		this.name = name;
	}

	public ForumBean(String name, String description, CategoryBean category) {
		this();
		this.name = name;
		this.description = description;
		this.category = category;
	}

	private CategoryBean category;

	private String description;

	private Integer id;

	private String name;

	private int order;

	private int topicCount;

	private int postCount;

	private int status;

	private List<TopicBean> topics;

	private Collection<WatchBean> watch;

	private List<WatchBean> watches;

	/**
	 * @return the category of teh forum
	 */
	public CategoryBean getCategory() {
		return category;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param category DOCUMENT_ME
	 */
	public void setCategory(CategoryBean category) {
		this.category = category;
	}

	/**
	 * @return the description of the forum
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param description DOCUMENT_ME
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the id of the forum
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param lastPost DOCUMENT_ME
	 */
	public void setLastPost(PostBean lastPost) {
	}

	/**
	 * @return the name of the forum
	 */
	public String getName() {
		return name;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param name DOCUMENT_ME
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the order number of the forum
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param order DOCUMENT_ME
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * @return the number of topics of the forum
	 */
	public int getTopicCount() {
		return topicCount;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param size DOCUMENT_ME
	 */
	public void setTopicCount(int size) {
		this.topicCount = size;
	}

	/**
	 * DOCUMENT_ME
	 */
	public void addTopicSize() {
		setTopicCount(topicCount + 1);
	}

	/**
	 * @return the posts count of the forum
	 */
	public int getPostCount() {
		return postCount;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param size DOCUMENT_ME
	 */
	public void setPostCount(int size) {
		this.postCount = size;
	}

	/**
	 * DOCUMENT_ME
	 */
	public void addPostSize() {
		setPostCount(postCount + 1);
	}

	/**
	 * _@___ hibernate.property column="jbp_prune_enable" unique="false"
	 * update="true"
	 * 
	 * @return the prune enable
	 */
	public boolean getPruneEnable() {
		// return pruneEnable;
		return false;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param enable DOCUMENT_ME
	 */
	public void setPruneEnable(boolean enable) {
	}

	/**
	 * _@___ hibernate.property column="jbp_prune_next" unique="false" update="true"
	 * 
	 * @return the next prune of the forum
	 */
	public int getPruneNext() {
		return 0;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param next DOCUMENT_ME
	 */
	public void setPruneNext(int next) {
	}

	/**
	 * @return the status of the forum
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param status DOCUMENT_ME
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the topics of the forum
	 */
	public List<TopicBean> getTopics() {
		return topics;

	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param value DOCUMENT_ME
	 */
	public void setTopics(List<TopicBean> value) {
		topics = value;
	}

	/**
	 * @return the forum watches of the forum
	 */
	public Collection<WatchBean> getForumWatch() {
		return watch;
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param watch DOCUMENT_ME
	 */
	public void setForumWatch(Collection<WatchBean> watch) {
		this.watch = watch;
	}

	/**
	 * @return the watches of the forum
	 */
	public List<WatchBean> getWatches() {
		return watches;
	}

	public void setWatches(List<WatchBean> watches) {
		this.watches = watches;
	}
}