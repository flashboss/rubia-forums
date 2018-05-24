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
package it.vige.rubia.wildfly.auth;

import static org.jboss.logging.Logger.getLogger;
import static org.jboss.security.PicketBoxMessages.MESSAGES;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.logging.Logger;
import org.jboss.security.acl.ACL;
import org.jboss.security.acl.ACLEntry;
import org.jboss.security.acl.ACLEntryImpl;
import org.jboss.security.acl.ACLImpl;
import org.jboss.security.acl.ACLPersistenceStrategy;
import org.jboss.security.acl.ACLResourceFactory;
import org.jboss.security.acl.Util;
import org.jboss.security.authorization.Resource;

/**
 * <p>
 * Implementation of {@code ACLPersistenceStrategy} that uses the Java
 * Persistence API (JPA) to persist the {@code ACL}s.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public class ForumsJPAPersistenceStrategy implements ACLPersistenceStrategy {

	private static Logger log = getLogger(ForumsJPAPersistenceStrategy.class);

	// in memory cache of the created ACLs.
	private final Map<Resource, ACL> aclMap;

	private final EntityManager manager;

	private final ACLResourceFactory resourceFactory;

	public ForumsJPAPersistenceStrategy(EntityManager manager) {
		this.aclMap = new HashMap<Resource, ACL>();
		this.manager = manager;
		this.resourceFactory = null;
	}

	/**
	 * @param resource
	 *            the resource to create
	 * @return the referred ACL
	 */
	@Override
	public ACL createACL(Resource resource) {
		return createACL(resource, new ArrayList<ACLEntry>());
	}

	/**
	 * @param resource
	 *            the resource to create
	 * @param entries
	 *            the entries for the ACL
	 * @return the referred ACL
	 */
	@Override
	public ACL createACL(Resource resource, Collection<ACLEntry> entries) {
		if (resource == null)
			throw MESSAGES.invalidNullArgument("resource");

		// check the cache first.
		ACL acl = aclMap.get(resource);
		if (acl == null) {
			try {
				// create a new ACL and persist it to the database.
				acl = new ACLImpl(resource, entries);
				manager.persist(acl);
				// add the newly-created ACL to the cache.
				aclMap.put(resource, acl);
			} catch (RuntimeException e) {
				log.error(e);
			}
		}
		return acl;
	}

	/**
	 * @param acl
	 *            the ACL to remove
	 * @return true if the ACL is removed with success
	 */
	@Override
	public boolean removeACL(ACL acl) {
		return removeACL(acl.getResource());
	}

	/**
	 * @param resource
	 *            the resource to remove
	 * 
	 * @return true if the resource is removed with success
	 */
	@Override
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
		} catch (RuntimeException e) {
			log.error(e);
		}
		return result;
	}

	/**
	 * @param resource
	 *            the resource
	 * @return the referred ACL for the resource
	 */
	@Override
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

	/**
	 * @return the current persisted ACLs
	 */
	@Override
	public Collection<ACL> getACLs() {
		Collection<ACL> acls = null;
		acls = manager.createQuery("SELECT a FROM ACLImpl a", ACL.class).getResultList();
		if (acls != null && resourceFactory != null) {
			for (ACL acl : acls) {
				ACLImpl impl = (ACLImpl) acl;
				String[] resourceName = impl.getResourceAsString().split(":");
				impl.setResource(resourceFactory.instantiateResource(resourceName[0], resourceName[1]));
			}
		}
		return acls;
	}

	/**
	 * @param acl
	 *            the ACL to update
	 * @return true if the ACL is updated with success
	 */
	@Override
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
		} catch (RuntimeException e) {
			log.error(e);
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
	 *            the {@code Resource} that is associated with the {@code ACL} being
	 *            searched.
	 * @param entityManager
	 *            the {@code EntityManager} used to search the database.
	 * @return the {@code ACL} retrieved from the database, or {@code null} if no
	 *         {@code ACL} could be found.
	 */
	private ACLImpl findACLByResource(Resource resource, EntityManager entityManager) {
		ACLImpl acl = null;
		try {
			acl = (ACLImpl) entityManager.createQuery("SELECT a FROM ACLImpl a WHERE a.resourceAsString LIKE '"
					+ Util.getResourceAsString(resource) + "'").getSingleResult();
			acl.setResource(resource);
		} catch (NoResultException nre) {
			// ignore the exception when no ACL could be found for the given
			// resource.
		}
		return acl;
	}
}
