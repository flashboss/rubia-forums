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
