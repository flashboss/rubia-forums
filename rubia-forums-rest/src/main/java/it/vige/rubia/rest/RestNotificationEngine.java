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

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import it.vige.rubia.util.NotificationEngine;

@Path("/notification/")
public class RestNotificationEngine implements NotificationEngine {

	@EJB
	private NotificationEngine notificationEngine;

	@GET
	@Path("stop")
	@Override
	public void stop() {
		notificationEngine.stop();
	}

	@GET
	@Path("setFrom/{from}")
	@Override
	public void setFrom(@PathParam("from") String from) {
		notificationEngine.setFrom(from);
	}

	@GET
	@Path("scheduleForNotification/{postId}/{mode}")
	@Override
	public void scheduleForNotification(@PathParam("postId") Integer postId, @PathParam("mode") int mode) {
		notificationEngine.scheduleForNotification(postId, mode);
	}

	@GET
	@Path("schedule/{postId}/{mode}/{absViewURL}/{absReplyURL}")
	@Override
	public void schedule(@PathParam("postId") Integer postId, @PathParam("mode") int mode,
			@PathParam("absViewURL") String absViewURL, @PathParam("absReplyURL") String absReplyURL) {
		notificationEngine.schedule(postId, mode, absViewURL, absReplyURL);
	}

}
