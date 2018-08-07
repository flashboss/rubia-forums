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

public class PollVotedBean implements Serializable {

	private int poll2;

	private int voted;

	private PollBean poll;

	private static final long serialVersionUID = 1L;

	public PollVotedBean() {
		super();
	}

	public PollBean getPoll() {
		return poll;
	}

	public void setPoll(PollBean poll) {
		this.poll = poll;
	}

	public int getPoll2() {
		return poll2;
	}

	public void setPoll2(int poll2) {
		this.poll2 = poll2;
	}

	public int getPollVoted() {
		return voted;
	}

	public void setPollVoted(int pollVoted) {
		this.voted = pollVoted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + poll2;
		result = prime * result + voted;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PollVotedBean other = (PollVotedBean) obj;
		if (poll2 != other.poll2)
			return false;
		if (voted != other.voted)
			return false;
		return true;
	}

}
