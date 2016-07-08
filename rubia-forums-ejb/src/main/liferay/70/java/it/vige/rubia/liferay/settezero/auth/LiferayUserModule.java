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
package it.vige.rubia.liferay.settezero.auth;

import static com.liferay.portal.service.UserServiceUtil.addUser;
import static com.liferay.portal.service.UserServiceUtil.getUserByScreenName;

import java.io.Serializable;

import javax.ejb.EJBContext;
import javax.ejb.Singleton;
import javax.inject.Named;

import com.liferay.portal.kernel.exception.PortalException;

import it.vige.rubia.auth.User;
import it.vige.rubia.auth.UserModule;

@Named("userModule")
@Singleton
public class LiferayUserModule implements UserModule, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8560321558665446098L;

	@javax.annotation.Resource
	private EJBContext ejbContext;

	@Override
	public User findUserByUserName(String arg0) throws IllegalArgumentException {
		loadIdentityManager();
		User user = null;
		try {
			com.liferay.portal.model.User newUser = getUser(arg0);
			user = new LiferayUser(newUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User findUserById(String arg0) throws IllegalArgumentException {
		loadIdentityManager();
		User user = null;
		try {
			com.liferay.portal.model.User newUser = getUser(arg0);
			user = new LiferayUser(newUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	private com.liferay.portal.model.User getUser(String userId) {
		try {
			return getUserByScreenName(0, userId);
		} catch (PortalException e) {
			return null;
		}
	}

	private void loadIdentityManager() {
		if (getUser("root") == null) {
			insertUser("root");
			insertUser("mary");
			insertUser("john");
			insertUser("demo");
		}
	}

	private void insertUser(String loginName) {
		com.liferay.portal.model.User user = getUser(loginName);
		if (user == null) {
			try {
				addUser(0, true, loginName, loginName, true, loginName, loginName, 0, loginName, null, loginName,
						loginName, loginName, 0, 0, true, 0, 0, 0, loginName, new long[0], new long[0], new long[0],
						new long[0], true, null);
			} catch (PortalException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean isGuest() {
		boolean anonymous = true;
		String remoteUser = ejbContext.getCallerPrincipal().getName();
		if (remoteUser != null && !remoteUser.isEmpty()) {
			anonymous = false;
		}
		return anonymous;
	}
}
