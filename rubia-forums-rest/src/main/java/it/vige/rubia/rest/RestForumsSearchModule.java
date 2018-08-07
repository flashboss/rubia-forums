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

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import it.vige.rubia.ModuleException;
import it.vige.rubia.dto.PostBean;
import it.vige.rubia.dto.TopicBean;
import it.vige.rubia.search.ForumsSearchModule;
import it.vige.rubia.search.ResultPage;
import it.vige.rubia.search.SearchCriteria;

@Path("/search/")
public class RestForumsSearchModule implements ForumsSearchModule {

	@EJB
	private ForumsSearchModule forumsSearchModule;

	@POST
	@Path("findPosts")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public ResultPage<PostBean> findPosts(SearchCriteria criteria) throws ModuleException {
		// TODO Auto-generated method stub
		return forumsSearchModule.findPosts(criteria);
	}

	@POST
	@Path("findTopics")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Override
	public ResultPage<TopicBean> findTopics(SearchCriteria criteria) throws ModuleException {
		// TODO Auto-generated method stub
		return forumsSearchModule.findTopics(criteria);
	}

}
