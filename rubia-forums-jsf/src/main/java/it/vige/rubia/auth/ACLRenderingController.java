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

import static it.vige.rubia.PortalUtil.getUser;

import java.util.StringTokenizer;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("aclRenderingController")
@RequestScoped
public class ACLRenderingController {

	@Inject
	private ForumsACLProvider forumsACLProvider;

	@Inject
	private UserModule userModule;

	public boolean aclCheck(String fragment, Object contextData) {
		boolean isAccessAllowed;
		// resourcesetup
		try {
			Object[] runtime = new Object[1];
			if (contextData != null) {
				runtime[0] = contextData;
			}

			// check access here
			UIContext securityContext = new UIContext(getUser(userModule));
			securityContext.setFragment(fragment);
			securityContext.setContextData(runtime);
			isAccessAllowed = forumsACLProvider.hasAccess(securityContext);
		} catch (NoSuchMethodException nsme) {
			throw new FacesException(nsme);
		} catch (Exception e) {
			throw new FacesException(e);
		}

		// feed this context to the Authorization system which will decide
		// whether
		// access should be granted or not
		return isAccessAllowed;
	}

	public boolean aclCheck(String fragment, String contextData, ForumsACLProvider forumsACLProvider,
			UserModule userModule, FaceletContext ctx) {
		boolean isAccessAllowed;
		// resourcesetup
		try {
			Object[] runtime = null;
			FacesContext facesContext = ctx.getFacesContext();
			if (contextData != null && contextData.trim().length() > 0) {
				StringTokenizer st = new StringTokenizer(contextData, ",");
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
			securityContext.setFragment(fragment);
			securityContext.setContextData(runtime);
			isAccessAllowed = forumsACLProvider.hasAccess(securityContext);
		} catch (NoSuchMethodException nsme) {
			throw new FacesException(nsme);
		} catch (Exception e) {
			throw new FacesException(e);
		}

		// feed this context to the Authorization system which will decide
		// whether
		// access should be granted or not
		return isAccessAllowed;
	}
}
