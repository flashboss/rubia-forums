package org.rubia.forums.web.urls;

import static org.rubia.forums.ui.JSFUtil.getContextPath;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;

import org.rubia.forums.ui.BaseController;
import org.rubia.forums.ui.UrlContext;

@Named("urlContext")
@RequestScoped
public class WebUrlContext extends BaseController implements UrlContext {

	private static final long serialVersionUID = -8150406584781434249L;

	public String outputLink(String outputLink, boolean isAction) {
		try {
			String url = null;

			if (!outputLink.startsWith("/")) {
				outputLink = "/" + outputLink;
			}

			String contextPath = getContextPath();
			url = contextPath + outputLink;

			return url;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}