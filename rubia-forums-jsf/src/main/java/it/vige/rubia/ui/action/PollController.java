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

import static it.vige.rubia.ui.ForumUtil.getParameter;
import static it.vige.rubia.ui.JSFUtil.getPoster;
import static it.vige.rubia.ui.JSFUtil.handleException;
import static java.lang.Integer.parseInt;

import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.auth.AuthorizationListener;
import it.vige.rubia.auth.SecureActionForum;
import it.vige.rubia.auth.UserModule;
import it.vige.rubia.model.Poll;
import it.vige.rubia.model.PollOption;
import it.vige.rubia.model.Poster;
import it.vige.rubia.model.Topic;
import it.vige.rubia.ui.BaseController;

/**
 * Created on May 5, 2006
 * 
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Named
public class PollController extends BaseController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5114581627888779169L;
	@Inject
	private ForumsModule forumsModule;
	@Inject
	private UserModule userModule;

	/**
	 * accepts a vote and processes it
	 * 
	 */
	@SecureActionForum
	@Interceptors(AuthorizationListener.class)
	public String vote() {
		String navState = null;
		try {
			String t = getParameter(p_topicId);
			String vote = getParameter(p_vote);

			if (t != null && t.trim().length() > 0) {
				// setup the data needed for this process
				int topicId, voteIndex;
				try {
					topicId = parseInt(t);
					voteIndex = parseInt(vote);
				} catch (NumberFormatException e) {
					// dont process a vote
					return null;
				}

				Topic topic = forumsModule.findTopicById(topicId);
				Poll poll = topic.getPoll();
				PollOption selectedOption = poll.getOptions().get(voteIndex);
				Poster poster = getPoster(userModule, forumsModule);

				// perform the voting on the selected option
				if (poster != null) {
					poll.getVoted().add(poster.getUserId());
				}
				selectedOption.incVotes();
				forumsModule.update(selectedOption);
			}
		} catch (Exception e) {
			handleException(e);
		}
		return navState;
	}
}
