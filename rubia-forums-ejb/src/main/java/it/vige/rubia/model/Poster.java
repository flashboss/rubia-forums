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
package it.vige.rubia.model;

import static org.hibernate.search.annotations.Index.YES;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Normalizer;
import org.hibernate.search.annotations.NormalizerDef;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.TokenFilterDef;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @version $Revision: 2811 $
 */

@NamedQueries({ @NamedQuery(name = "findPosterByUserId", query = "select u from Poster as u where u.userId=:userId") })
@Entity
@Table(name = "JBP_FORUMS_POSTERS")
@NormalizerDef(name = "userId_lowercase", filters = { @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
		@TokenFilterDef(factory = LowerCaseFilterFactory.class) })
public class Poster {

	@Id
	@Column(name = "JBP_ID")
	@GeneratedValue
	private Integer id;

	@Field(name = "userId", index = YES)
	@Field(name = "userId_order", index = YES, normalizer = @Normalizer(definition = "userId_lowercase"))
	@Column(name = "JBP_USER_ID")
	@SortableField(forField = "userId_order")
	private String userId;

	@Column(name = "JBP_POST_COUNT")
	private int nbPosts = 0;

	/**
	 * Creates a new {@link Poster} object.
	 */
	public Poster() {
	}

	public Poster(String userId) {
		setUserId(userId);
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This column has the unique constraints as it reflects the user id.
	 * 
	 * @return Returns the id.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId The id to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the posts count of the poster
	 */
	public int getPostCount() {
		return nbPosts;
	}

	private void setPostCount(int nbPosts) {
		this.nbPosts = nbPosts;
	}

	/**
	 * DOCUMENT_ME
	 */
	public void incrementPostCount() {
		setPostCount(nbPosts + 1);
	}
}
