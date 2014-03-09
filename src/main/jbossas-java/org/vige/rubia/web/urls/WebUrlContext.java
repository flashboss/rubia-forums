package org.vige.rubia.web.urls;

import static org.vige.rubia.ui.JSFUtil.getContextPath;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;

import org.vige.rubia.ui.BaseController;
import org.vige.rubia.ui.UrlContext;

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