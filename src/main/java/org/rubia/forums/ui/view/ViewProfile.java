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
package org.rubia.forums.ui.view;

import static org.rubia.forums.ui.ForumUtil.getParameter;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.rubia.forums.ForumsModule;
import org.rubia.forums.model.Poster;
import org.rubia.forums.ui.BaseController;

/**
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Named("profile")
public class ViewProfile extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2028734088051699739L;

	@EJB(name = "jboss-forums/ForumsModuleImpl/local")
	private ForumsModule forumsModule;

	private Poster poster;

	public Poster getPoster() {
		return poster;
	}

	// ui actions supported by this
	// bean----------------------------------------------------------------------------------------------------
	/**
	 * This method gets userId from parameters and tries to find Poster with
	 * this uid.
	 */
	@PostConstruct
	public void execute() throws Exception {
		String userId = getParameter(p_userId);
		if (userId != null && !userId.trim().equals("")) {
			poster = forumsModule.findPosterByUserId(userId);
		}
	}

}
