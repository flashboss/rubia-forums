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
package org.vige.rubia.eap.seiunouno.auth;

import static org.vige.rubia.ui.JSFUtil.isAnonymous;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateful;
import javax.inject.Named;

import org.vige.rubia.auth.User;
import org.vige.rubia.auth.UserModule;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.file.internal.FileIdentityStoreConfiguration;
import org.picketlink.idm.internal.DefaultIdentityManager;
import org.picketlink.idm.internal.SimpleIdentityStoreInvocationContextFactory;
import org.picketlink.idm.model.Agent;
import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.Partition;
import org.picketlink.idm.spi.IdentityStoreInvocationContextFactory;

@Named("userModule")
@Stateful
public class JBossUserModule implements UserModule, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8560321558665446098L;
	private IdentityManager identityManager;

	@Override
	public User findUserByUserName(String arg0) throws IllegalArgumentException {
		loadUsers();
		User user = null;
		try {
			org.picketlink.idm.model.User newUser = identityManager.getUser(arg0);
			user = new JBossUser(newUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User findUserById(String arg0) throws IllegalArgumentException {
		loadUsers();
		User user = null;
		try {
			org.picketlink.idm.model.User newUser = identityManager.getUser(arg0);
			user = new JBossUser(newUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	private void loadUsers() {
		if (identityManager == null) {
			identityManager = new DefaultIdentityManager();
			IdentityConfiguration configuration = new IdentityConfiguration();
			FileIdentityStoreConfiguration config = new FileIdentityStoreConfiguration() {

				Map<String, Agent> users = new HashMap<String, Agent>();
				{
					users.put("root", new org.picketlink.idm.model.User() {

						/**
						 * 
						 */
						private static final long serialVersionUID = -3782194063515168210L;

						@Override
						public String getId() {
							// TODO Auto-generated method stub
							return "root";
						}

						@Override
						public String getKey() {
							// TODO Auto-generated method stub
							return "root";
						}

						@Override
						public boolean isEnabled() {
							// TODO Auto-generated method stub
							return true;
						}

						@Override
						public void setEnabled(boolean enabled) {
							// TODO Auto-generated method stub

						}

						@Override
						public Date getCreatedDate() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setCreatedDate(Date createdDate) {
							// TODO Auto-generated method stub

						}

						@Override
						public Date getExpirationDate() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setExpirationDate(Date expirationDate) {
							// TODO Auto-generated method stub

						}

						@Override
						public void setAttribute(Attribute<? extends Serializable> attribute) {
							// TODO Auto-generated method stub

						}

						@Override
						public void removeAttribute(String name) {
							// TODO Auto-generated method stub

						}

						@Override
						public <T extends Serializable> Attribute<T> getAttribute(String name) {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public Collection<Attribute<? extends Serializable>> getAttributes() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public Partition getPartition() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setPartition(Partition partition) {
							// TODO Auto-generated method stub

						}

						@Override
						public String getFirstName() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setFirstName(String firstName) {
							// TODO Auto-generated method stub

						}

						@Override
						public String getLastName() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setLastName(String lastName) {
							// TODO Auto-generated method stub

						}

						@Override
						public String getEmail() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setEmail(String email) {
							// TODO Auto-generated method stub

						}

					});
					users.put("mary", new org.picketlink.idm.model.User() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 5387839606923392628L;

						@Override
						public String getId() {
							// TODO Auto-generated method stub
							return "mary";
						}

						@Override
						public String getKey() {
							// TODO Auto-generated method stub
							return "mary";
						}

						@Override
						public boolean isEnabled() {
							// TODO Auto-generated method stub
							return true;
						}

						@Override
						public void setEnabled(boolean enabled) {
							// TODO Auto-generated method stub

						}

						@Override
						public Date getCreatedDate() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setCreatedDate(Date createdDate) {
							// TODO Auto-generated method stub

						}

						@Override
						public Date getExpirationDate() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setExpirationDate(Date expirationDate) {
							// TODO Auto-generated method stub

						}

						@Override
						public void setAttribute(Attribute<? extends Serializable> attribute) {
							// TODO Auto-generated method stub

						}

						@Override
						public void removeAttribute(String name) {
							// TODO Auto-generated method stub

						}

						@Override
						public <T extends Serializable> Attribute<T> getAttribute(String name) {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public Collection<Attribute<? extends Serializable>> getAttributes() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public Partition getPartition() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setPartition(Partition partition) {
							// TODO Auto-generated method stub

						}

						@Override
						public String getFirstName() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setFirstName(String firstName) {
							// TODO Auto-generated method stub

						}

						@Override
						public String getLastName() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setLastName(String lastName) {
							// TODO Auto-generated method stub

						}

						@Override
						public String getEmail() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setEmail(String email) {
							// TODO Auto-generated method stub

						}

					});
					users.put("john", new org.picketlink.idm.model.User() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 5387839606923392628L;

						@Override
						public String getId() {
							// TODO Auto-generated method stub
							return "john";
						}

						@Override
						public String getKey() {
							// TODO Auto-generated method stub
							return "john";
						}

						@Override
						public boolean isEnabled() {
							// TODO Auto-generated method stub
							return true;
						}

						@Override
						public void setEnabled(boolean enabled) {
							// TODO Auto-generated method stub

						}

						@Override
						public Date getCreatedDate() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setCreatedDate(Date createdDate) {
							// TODO Auto-generated method stub

						}

						@Override
						public Date getExpirationDate() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setExpirationDate(Date expirationDate) {
							// TODO Auto-generated method stub

						}

						@Override
						public void setAttribute(Attribute<? extends Serializable> attribute) {
							// TODO Auto-generated method stub

						}

						@Override
						public void removeAttribute(String name) {
							// TODO Auto-generated method stub

						}

						@Override
						public <T extends Serializable> Attribute<T> getAttribute(String name) {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public Collection<Attribute<? extends Serializable>> getAttributes() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public Partition getPartition() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setPartition(Partition partition) {
							// TODO Auto-generated method stub

						}

						@Override
						public String getFirstName() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setFirstName(String firstName) {
							// TODO Auto-generated method stub

						}

						@Override
						public String getLastName() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setLastName(String lastName) {
							// TODO Auto-generated method stub

						}

						@Override
						public String getEmail() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setEmail(String email) {
							// TODO Auto-generated method stub

						}

					});
					users.put("demo", new org.picketlink.idm.model.User() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 5387839606923392628L;

						@Override
						public String getId() {
							// TODO Auto-generated method stub
							return "demo";
						}

						@Override
						public String getKey() {
							// TODO Auto-generated method stub
							return "demo";
						}

						@Override
						public boolean isEnabled() {
							// TODO Auto-generated method stub
							return true;
						}

						@Override
						public void setEnabled(boolean enabled) {
							// TODO Auto-generated method stub

						}

						@Override
						public Date getCreatedDate() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setCreatedDate(Date createdDate) {
							// TODO Auto-generated method stub

						}

						@Override
						public Date getExpirationDate() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setExpirationDate(Date expirationDate) {
							// TODO Auto-generated method stub

						}

						@Override
						public void setAttribute(Attribute<? extends Serializable> attribute) {
							// TODO Auto-generated method stub

						}

						@Override
						public void removeAttribute(String name) {
							// TODO Auto-generated method stub

						}

						@Override
						public <T extends Serializable> Attribute<T> getAttribute(String name) {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public Collection<Attribute<? extends Serializable>> getAttributes() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public Partition getPartition() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setPartition(Partition partition) {
							// TODO Auto-generated method stub

						}

						@Override
						public String getFirstName() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setFirstName(String firstName) {
							// TODO Auto-generated method stub

						}

						@Override
						public String getLastName() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setLastName(String lastName) {
							// TODO Auto-generated method stub

						}

						@Override
						public String getEmail() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public void setEmail(String email) {
							// TODO Auto-generated method stub

						}

					});
				}

				@Override
				public Map<String, Agent> getUsers() {
					return users;
				}

			};
			config.init();
			configuration.addStoreConfiguration(config);
			IdentityStoreInvocationContextFactory contextFactory = new SimpleIdentityStoreInvocationContextFactory();
			identityManager.bootstrap(configuration, contextFactory);
		}

	}

	@Override
	public boolean isGuest() {
		// TODO Auto-generated method stub
		return isAnonymous();
	}
}
