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
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet </a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute </a>
 * @author <a href="mailto:boleslaw.dawidowicz@jboss.com">Boleslaw
 *         Dawidowicz</a>
 * @version $Revision: 916 $
 */

public class PollBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8806095549733743538L;

	/**
	 * Creates a new {@link PollBean} object.
	 */
	public PollBean() {
		setOptions(new LinkedList<PollOptionBean>());
		setVoted(new HashSet<String>());
	}

	public PollBean(String title, List<PollOptionBean> options, int length) {
		this();
		this.title = title;
		this.options = options;
		this.length = length;
	}

	private Integer id;

	private String title;

	private List<PollOptionBean> options;

	private int length;

	private Set<String> voted;

	private Date creationDate;

	/**
	 * @return the id of the poll
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param string the title of the poll
	 */
	public void setTitle(String string) {
		this.title = string;
	}

	/**
	 * 
	 * @param i the length of the poll
	 */
	public void setLength(int i) {
		this.length = i;
	}

	/**
	 * 
	 * @param list the list of poll options
	 */
	public void setOptions(List<PollOptionBean> list) {
		this.options = list;
	}

	/**
	 * @return the poll options of the poll
	 */
	public List<PollOptionBean> getOptions() {
		return this.options;
	}

	/**
	 * @return the title of the poll
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return the length of the poll
	 */
	public int getLength() {
		return this.length;
	}

	/**
	 * @return the voted questions of the poll
	 */
	public Set<String> getVoted() {
		return voted;
	}

	public void setVoted(Set<String> voted) {
		this.voted = voted;
	}

	// TODO: Can be optimized
	public int getVotesSum() {
		int sum = 0;
		for (Iterator<PollOptionBean> i = getOptions().iterator(); i.hasNext();) {
			sum += ((PollOptionBean) i.next()).getVotes();
		}
		return sum;
	}

	/**
	 * @return the creation date of the poll
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}