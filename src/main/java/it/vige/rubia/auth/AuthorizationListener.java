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

import static it.vige.rubia.ui.PortalUtil.getUser;
import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/*
 * Created on May 17, 2006
 *
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 */
@SecureActionForum
@Interceptor
public class AuthorizationListener implements Serializable {

	private static final long serialVersionUID = 1297507762601849153L;

	@Inject
	private ForumsACLProvider forumsACLProvider;

	@Inject
	private UserModule userModule;

	@AroundInvoke
	public Object accessAllowed(InvocationContext ctx) throws Exception {
		Method businessAction = ctx.getMethod();
		Object managedBean = ctx.getTarget();
		boolean isAccessAllowed = false;
		FacesContext facesContext = getCurrentInstance();

		// enforce authorization security
		try {

			// start building the SecurityContext here for the Authorization
			// System
			JSFActionContext securityContext = new JSFActionContext(
					getUser(userModule), facesContext);
			securityContext.setBusinessAction(businessAction);
			securityContext.setManagedBean(managedBean);

			// feed this context to the Authorization system which will decide
			// whether
			// access should be granted or not
			isAccessAllowed = forumsACLProvider.hasAccess(securityContext);
			if (!isAccessAllowed)
				return null;
		} catch (NoSuchMethodException nsme) {
			throw new FacesException(
					"Error calling action method of component with id " + nsme,
					nsme);
		} catch (Exception e) {
			throw new FacesException(
					"Error calling action method of component with id " + e, e);
		}
		return ctx.proceed();
	}
}
