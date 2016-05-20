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

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJBContext;
import javax.ejb.Singleton;
import javax.inject.Named;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.internal.DefaultPartitionManager;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.idm.query.AttributeParameter;
import org.picketlink.idm.query.Condition;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.picketlink.idm.query.QueryParameter;

import it.vige.rubia.auth.User;
import it.vige.rubia.auth.UserModule;

@Named("userModule")
@Singleton
public class LiferayUserModule implements UserModule, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8560321558665446098L;
	private IdentityManager identityManager;

	@javax.annotation.Resource
	private EJBContext ejbContext;

	@Override
	public User findUserByUserName(String arg0) throws IllegalArgumentException {
		loadIdentityManager();
		User user = null;
		try {
			org.picketlink.idm.model.basic.User newUser = getUser(arg0);
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
			org.picketlink.idm.model.basic.User newUser = getUser(arg0);
			user = new LiferayUser(newUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	private org.picketlink.idm.model.basic.User getUser(String userId) {
		IdentityQueryBuilder identityQueryBuilder = identityManager.getQueryBuilder();
		IdentityQuery<org.picketlink.idm.model.basic.User> query = identityQueryBuilder
				.createIdentityQuery(org.picketlink.idm.model.basic.User.class);
		QueryParameter id = new AttributeParameter("loginName");
		Condition condition = identityQueryBuilder.equal(id, userId);
		query.where(condition);
		List<org.picketlink.idm.model.basic.User> newUsers = query.getResultList();
		if (newUsers.size() > 0)
			return newUsers.get(0);
		else
			return null;
	}

	private void loadIdentityManager() {
		if (identityManager == null) {
			IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();
			builder.named("file-store-preserve-state").stores().file().preserveState(true).supportAllFeatures();
			PartitionManager partitionManager = new DefaultPartitionManager(builder.buildAll());
			Realm realm = partitionManager.getPartition(Realm.class, "forums-realm");
			if (realm == null) {
				realm = new Realm("forums-realm");
				partitionManager.add(realm);
			}
			identityManager = partitionManager.createIdentityManager(realm);
			insertUser("root");
			insertUser("mary");
			insertUser("john");
			insertUser("demo");
		}
	}

	private void insertUser(String loginName) {
		org.picketlink.idm.model.basic.User user = getUser(loginName);
		if (user == null) {
			user = new org.picketlink.idm.model.basic.User(loginName);
			identityManager.add(user);
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
