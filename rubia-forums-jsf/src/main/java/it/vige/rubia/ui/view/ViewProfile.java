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
package it.vige.rubia.ui.view;

import static it.vige.rubia.ui.ForumUtil.getParameter;
import static org.jboss.logging.Logger.getLogger;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.inject.Named;

import org.jboss.logging.Logger;

import it.vige.rubia.ForumsModule;
import it.vige.rubia.ModuleException;
import it.vige.rubia.dto.PosterBean;
import it.vige.rubia.ui.BaseController;

/**
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
@Named("profile")
public class ViewProfile extends BaseController {

	private static final long serialVersionUID = 2028734088051699739L;
	private static Logger log = getLogger(ViewProfile.class);

	@EJB(name = "jboss-forums/ForumsModuleImpl/local")
	private ForumsModule forumsModule;

	private PosterBean poster;

	public PosterBean getPoster() {
		return poster;
	}

	// ui actions supported by this
	// bean----------------------------------------------------------------------------------------------------
	/**
	 * This method gets userId from parameters and tries to find Poster with this
	 * uid.
	 */
	@PostConstruct
	public void execute() {
		String userId = getParameter(p_userId);
		if (userId != null && !userId.trim().equals("")) {
			try {
				poster = forumsModule.findPosterByUserId(userId);
			} catch (ModuleException e) {
				log.error(e);
			}
		}
	}

}
