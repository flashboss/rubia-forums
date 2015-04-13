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
package it.vige.rubia.gatein.treseizero.web.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.security.ErrorCodes;
import org.jboss.security.acl.ACL;
import org.jboss.security.acl.ACLEntry;
import org.jboss.security.acl.ACLPersistenceStrategy;
import org.jboss.security.acl.ACLResourceFactory;
import org.jboss.security.acl.Util;
import org.jboss.security.authorization.Resource;

public class ForumsJPAPersistenceStrategy implements ACLPersistenceStrategy {

	// in memory cache of the created ACLs.
	private final Map<Resource, ACL> aclMap;

	private final EntityManager manager;

	private final ACLResourceFactory resourceFactory;

	public ForumsJPAPersistenceStrategy(EntityManager manager) {
		this.aclMap = new HashMap<Resource, ACL>();
		this.manager = manager;
		this.resourceFactory = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.security.acl.ACLProvider#createACL(org.jboss.security.authorization
	 * .Resource)
	 */
	public ACL createACL(Resource resource) {
		return createACL(resource, new ArrayList<ACLEntry>());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.security.acl.ACLProvider#createACL(org.jboss.security.authorization
	 * .Resource, java.util.Collection)
	 */
	public ACL createACL(Resource resource, Collection<ACLEntry> entries) {
		if (resource == null)
			throw new IllegalArgumentException(ErrorCodes.NULL_ARGUMENT
					+ "ACLs cannot be created for null resources");

		// check the cache first.
		ACL acl = aclMap.get(resource);
		if (acl == null) {
			try {
				// create a new ACL and persist it to the database.
				acl = new ACLImpl(resource, entries);
				manager.persist(acl);
				// add the newly-created ACL to the cache.
				aclMap.put(resource, acl);
			} catch (RuntimeException re) {
				re.printStackTrace();
			}
		}
		return acl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.security.acl.ACLProvider#removeACL(org.jboss.security.acl.ACL)
	 */
	public boolean removeACL(ACL acl) {
		return removeACL(acl.getResource());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.security.acl.ACLProvider#removeACL(org.jboss.security.authorization
	 * .Resource)
	 */
	public boolean removeACL(Resource resource) {
		boolean result = false;

		try {
			// find the ACL associated with the specified resource and remove it
			// from the database.
			ACL acl = findACLByResource(resource, manager);
			if (acl != null) {
				manager.remove(acl);
				// remove the ACL from the cache.
				result = aclMap.remove(resource) != null;
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.security.acl.ACLProvider#getACL(org.jboss.security.authorization
	 * .Resource)
	 */
	public ACL getACL(Resource resource) {
		// check the cache first.
		ACL acl = aclMap.get(resource);
		if (acl == null) {
			acl = findACLByResource(resource, manager);
			if (acl != null)
				aclMap.put(resource, acl);
		}
		return acl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.security.acl.ACLPersistenceStrategy#getACLs()
	 */
	@SuppressWarnings("unchecked")
	public Collection<ACL> getACLs() {
		Collection<ACL> acls = null;
		acls = manager.createQuery("SELECT a FROM ACLImpl a").getResultList();
		if (acls != null && resourceFactory != null) {
			for (ACL acl : acls) {
				ACLImpl impl = (ACLImpl) acl;
				String[] resourceName = impl.getResourceAsString().split(":");
				impl.setResource(resourceFactory.instantiateResource(
						resourceName[0], resourceName[1]));
			}
		}
		return acls;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.security.acl.ACLProvider#updateACL(org.jboss.security.acl.ACL)
	 */
	public boolean updateACL(ACL acl) {
		if (((ACLImpl) acl).getACLId() == 0)
			return false;

		try {
			for (ACLEntry entry : acl.getEntries()) {
				// persist the new entries that might have been added to the
				// ACL.
				ACLEntryImpl entryImpl = (ACLEntryImpl) entry;
				if (entryImpl.getACLEntryId() == 0)
					manager.persist(entryImpl);
			}
			// merge will take care of the entries that might have been removed.
			manager.merge(acl);
			// update the cache.
			aclMap.put(acl.getResource(), acl);
			return true;
		} catch (RuntimeException re) {
			re.printStackTrace();
		}
		return false;
	}

	/**
	 * <p>
	 * Searches the database for the {@code ACL} associated with the specified
	 * resource.
	 * </p>
	 * 
	 * @param resource
	 *            the {@code Resource} that is associated with the {@code ACL}
	 *            being searched.
	 * @param entityManager
	 *            the {@code EntityManager} used to search the database.
	 * @return the {@code ACL} retrieved from the database, or {@code null} if
	 *         no {@code ACL} could be found.
	 */
	private ACLImpl findACLByResource(Resource resource,
			EntityManager entityManager) {
		ACLImpl acl = null;
		try {
	         acl = (ACLImpl) entityManager.createQuery(
	                 "SELECT a FROM ACLImpl a WHERE a.resourceAsString LIKE '" + Util.getResourceAsString(resource) + "'")
	                 .getSingleResult();
	           acl.setResource(resource);
		} catch (NoResultException nre) {
			// ignore the exception when no ACL could be found for the given
			// resource.
		}
		return acl;
	}
}
