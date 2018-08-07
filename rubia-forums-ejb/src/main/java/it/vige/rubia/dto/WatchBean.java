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

/**
 * Watchers for email notifications.
 * 
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 916 $
 */

public class WatchBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3026188040389764985L;

	private Integer id;

	private int mode;

	private PosterBean poster;

	/**
	 * @return the id of the watch
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the poster of the watch
	 */
	public PosterBean getPoster() {
		return poster;
	}

	public void setPoster(PosterBean poster) {
		this.poster = poster;
	}

	/**
	 * @return the mode of the watch
	 */
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
}
