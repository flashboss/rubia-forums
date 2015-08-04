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

/*
 * Created on May 23, 2006
 *
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 */
public class UIContext extends IdentitySecurityContext {
	/**
	 * 
	 *
	 */
	private String fragment;
	private Object[] contextData;

	/**
	 * 
	 * @param identity
	 *            the jboss security identity to assign
	 */
	public UIContext(Object identity) {
		super(identity);
	}

	/**
	 * @return Returns the contextData.
	 */
	public Object[] getContextData() {
		return contextData;
	}

	/**
	 * @param contextData
	 *            The contextData to set.
	 */
	public void setContextData(Object[] contextData) {
		this.contextData = contextData;
	}

	/**
	 * @return Returns the fragment.
	 */
	public String getFragment() {
		return fragment;
	}

	/**
	 * @param fragment
	 *            The fragment to set.
	 */
	public void setFragment(String fragment) {
		this.fragment = fragment;
	}
}
