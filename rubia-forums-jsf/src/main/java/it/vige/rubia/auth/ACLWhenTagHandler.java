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
package it.vige.rubia.auth;

//core java
import static it.vige.rubia.util.PortalUtil.getUser;

import java.io.IOException;
import java.util.StringTokenizer;

import jakarta.el.ELException;
import jakarta.el.ExpressionFactory;
import jakarta.el.ValueExpression;
import jakarta.faces.FacesException;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.TagAttribute;
import jakarta.faces.view.facelets.TagConfig;
import jakarta.faces.view.facelets.TagHandler;

/**
 * 
 * @author Sohil Shah - sohil.shah@jboss.com - Mar 29, 2006
 * 
 */
public class ACLWhenTagHandler extends TagHandler {

	// possible attributes
	private TagAttribute fragment; // required
	private TagAttribute contextData; // optional
	private TagAttribute forumsACLProviderAttr;
	private TagAttribute userModuleAttr;

	/**
	 * @param config
	 *            the configuration tag for the handler
	 */
	public ACLWhenTagHandler(TagConfig config) {
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
	public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, ELException {
		nextHandler.apply(ctx, parent);
	}

	/**
	 * @param ctx
	 *            the context to check
	 * @return true is the passed context is allowed
	 *
	 */
	public boolean isAllowed(FaceletContext ctx) {
		boolean isAllowed = false;

		FacesContext facesContext = ctx.getFacesContext();
		ForumsACLProvider forumsACLProvider = (ForumsACLProvider) forumsACLProviderAttr.getObject(ctx);
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
			isAllowed = true;
			return isAllowed;
		}

		// an authorization provider is hooked in....go ahead and perform
		// authorization
		try {
			String resource = fragment.getValue();
			String contextStr = null;

			if (contextData != null) {
				contextStr = contextData.getValue();
			}

			// resourcesetup
			Object[] runtime = null;
			if (contextStr != null && contextStr.trim().length() > 0) {
				StringTokenizer st = new StringTokenizer(contextStr, ",");
				runtime = new Object[st.countTokens()];
				int i = 0;
				while (st.hasMoreTokens()) {
					String parameter = st.nextToken();
					Object parameterValue = null;

					// evaluate this expression to a value
					ExpressionFactory f = ctx.getExpressionFactory();
					ValueExpression expr = f.createValueExpression(ctx, parameter, Object.class);
					parameterValue = expr.getValue(facesContext.getELContext());

					runtime[i++] = parameterValue;
				}
			}

			// check access here
			UIContext securityContext = new UIContext(getUser(userModule));
			securityContext.setFragment(resource);
			securityContext.setContextData(runtime);

			// feed this context to the Authorization system which will decide
			// whether
			// access should be granted or not
			isAllowed = forumsACLProvider.hasAccess(securityContext);
		} catch (NoSuchMethodException nsme) {
			throw new FacesException(nsme);
		} catch (Exception e) {
			throw new FacesException(e);
		}

		return isAllowed;
	}
}
