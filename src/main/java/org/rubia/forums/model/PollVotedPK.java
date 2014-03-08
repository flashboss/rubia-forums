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
package org.rubia.forums.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;

@Embeddable
public class PollVotedPK implements Serializable {
	@Column(name="JBP_POLL_ID")
	@GeneratedValue
	private int poll2;

	@Column(name="JBP_POLL_VOTED")
	private int voted;

	private static final long serialVersionUID = 1L;

	public PollVotedPK() {
		super();
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
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof PollVotedPK)) {
			return false;
		}
		PollVotedPK other = (PollVotedPK) o;
		return (this.poll2 == other.poll2)
			&& (this.voted == other.voted);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.poll2;
		hash = hash * prime + this.voted;
		return hash;
	}

}
