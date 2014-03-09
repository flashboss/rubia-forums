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
package org.vige.rubia.auth;

import java.io.IOException;

import javax.ejb.TransactionAttribute;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

public class ACLTagHandler extends TagHandler {

	// possible attributes
	private TagAttribute fragment; // required
	private TagAttribute contextData; // optional
	private TagAttribute forumsACLProviderAttr;
	private TagAttribute userModuleAttr;

	/**
	 * @param config
	 */
	public ACLTagHandler(TagConfig config) {
		super(config);

		// helper method for getting a required attribute
		fragment = getRequiredAttribute("fragment");

		// helper method, optional attribute
		contextData = getAttribute("contextData");

		// helper method, optional attribute
		forumsACLProviderAttr = getAttribute("forumsACLProvider");

		// helper method, optional attribute
		userModuleAttr = getAttribute("userModule");
	}

	/**
	 * Threadsafe Method for controlling evaluation of its child tags,
	 * represented by "nextHandler"
	 */
	@TransactionAttribute
	public void apply(FaceletContext ctx, UIComponent parent)
			throws IOException, FacesException, ELException {
		ForumsACLProvider forumsACLProvider = (ForumsACLProvider) forumsACLProviderAttr
				.getObject(ctx);
		UserModule userModule = (UserModule) userModuleAttr.getObject(ctx);
		// make sure an authorization provider has been hooked in
		boolean skipAuth = false;
		try {
			if (forumsACLProvider == null) {
				// no authorization will be enforced
				skipAuth = true;
			}
		} catch (Exception e) {
			// something went wrong in the Authorization system
			skipAuth = true;
		}

		if (skipAuth) {
			nextHandler.apply(ctx, parent);
			return;
		}

		// an authorization provider is hooked in....go ahead and perform
		// authorization
		String resource = fragment.getValue();
		String contextStr = null;

		if (this.contextData != null) {
			contextStr = contextData.getValue();
		}
		boolean isAccessAllowed = new ACLRenderingController().aclCheck(
				resource, contextStr, forumsACLProvider, userModule, ctx);

		if (isAccessAllowed) {
			nextHandler.apply(ctx, parent);
		}
	}
}