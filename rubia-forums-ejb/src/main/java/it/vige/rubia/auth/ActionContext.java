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

import java.lang.reflect.Method;

/*
 * Created on May 23, 2006
 *
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 */
public class ActionContext extends IdentitySecurityContext {
	/**
	 * this is the action method on a JSF Managed Bean that is being called and
	 * needs to be authorized access to
	 */
	private Method businessAction = null;

	/**
	 * This is the JSF Managed Bean that is being used
	 */
	private Object managedBean = null;

	/**
	 * 
	 * @param identity
	 *            the security identity to wrap
	 */
	public ActionContext(Object identity) {
		super(identity);
	}

	/**
	 * 
	 * @return the method for the current business action
	 */
	public Method getBusinessAction() {
		return this.businessAction;
	}

	/**
	 * 
	 * @param businessAction
	 *            the method of the business action to set
	 */
	public void setBusinessAction(Method businessAction) {
		this.businessAction = businessAction;
	}

	/**
	 * 
	 * @return the current managed bean
	 */
	public Object getManagedBean() {
		return this.managedBean;
	}

	/**
	 * 
	 * @param managedBean
	 *            the managed bean to set
	 */
	public void setManagedBean(Object managedBean) {
		this.managedBean = managedBean;
	}
}
