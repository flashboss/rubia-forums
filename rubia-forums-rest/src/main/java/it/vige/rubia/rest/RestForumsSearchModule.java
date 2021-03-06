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
package it.vige.rubia.rest;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import it.vige.rubia.ModuleException;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.search.ForumsSearchModule;
import it.vige.rubia.search.SearchCriteria;

@Path("/search/")
public class RestForumsSearchModule {

	@EJB
	private ForumsSearchModule forumsSearchModule;

	@POST
	@Path("findPosts")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<PostBean> findPosts(SearchCriteria criteria) throws ModuleException {
		return forumsSearchModule.findPosts(criteria).getPage();
	}

	@POST
	@Path("findTopics")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	public List<TopicBean> findTopics(SearchCriteria criteria) throws ModuleException {
		return forumsSearchModule.findTopics(criteria).getPage();
	}

}
