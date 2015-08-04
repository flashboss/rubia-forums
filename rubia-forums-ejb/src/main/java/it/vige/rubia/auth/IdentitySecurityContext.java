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
 * Created on May 19, 2006
 *
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 */
public abstract class IdentitySecurityContext implements SecurityContext {

	/**
	 * this is current user that needs to be authorized.. left the type of this
	 * identity open ended.. the actual provider can then cast it based on what
	 * it expects it to be
	 */
	private Object identity;

	/**
	 * @param identity
	 *            the security identity to set
	 *
	 */
	public IdentitySecurityContext(Object identity) {
		this.identity = identity;
	}

	/**
	 * @return Returns the identity.
	 */
	public Object getIdentity() {
		return identity;
	}

	/**
	 * @param identity
	 *            The identity to set.
	 */
	public void setIdentity(Object identity) {
		this.identity = identity;
	}
}
