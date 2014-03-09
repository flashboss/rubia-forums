package org.rubia.forums.portal.urls;

import static java.net.URLEncoder.encode;
import static org.rubia.forums.ui.PortalUtil.VIEW;
import static org.rubia.forums.ui.PortalUtil.getIdForName;

import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import org.rubia.forums.ui.BaseController;
import org.rubia.forums.ui.UrlContext;

@Named("urlContext")
@RequestScoped
public class PortalUrlContext extends BaseController implements UrlContext {

	private static final long serialVersionUID = -8150406584781434248L;

	public String outputLink(String outputLink, boolean isAction) {
		try {
			String url = null;

			if (!outputLink.startsWith("/")) {
				outputLink = "/" + outputLink;
			}

			Object response = FacesContext.getCurrentInstance()
					.getExternalContext().getResponse();
			RenderResponse renderResponse = (RenderResponse) response;

			PortletURL portletURL = null;

			if (isAction) {
				portletURL = renderResponse.createActionURL();
				portletURL.setSecure(false);
			} else {
				portletURL = renderResponse.createRenderURL();
			}

			url = portletURL.toString();

			return url;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}