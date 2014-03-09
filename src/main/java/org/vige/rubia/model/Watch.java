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
package org.vige.rubia.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Watchers for email notifications.
 * 
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 916 $
 */

@Entity
@Table(name = "JBP_FORUMS_WATCH")
@Inheritance(strategy = InheritanceType.JOINED)
public class Watch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3026188040389764985L;

	/**
	 * .
	 */
	@Id
	@Column(name = "JBP_ID")
	@GeneratedValue
	private Integer id;

	/**
	 * .
	 */
	@Column(name = "JBP_MODE")
	private int mode;

	/**
	 * .
	 */
	@ManyToOne
	@JoinColumn(name = "JBP_POSTER_ID")
	private Poster poster;

	/**
    */
	public Integer getId() {
		return id;
	}

	/**
    */
	public Poster getPoster() {
		return poster;
	}

	public void setPoster(Poster poster) {
		this.poster = poster;
	}

	/**
    */
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
}
