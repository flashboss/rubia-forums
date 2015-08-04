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
package it.vige.rubia.wildfly.ottoduezero.auth;

import static org.jboss.security.PicketBoxMessages.MESSAGES;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.jboss.security.acl.ACL;
import org.jboss.security.acl.ACLEntry;
import org.jboss.security.acl.ACLPermission;
import org.jboss.security.acl.Util;
import org.jboss.security.authorization.Resource;
import org.jboss.security.identity.Identity;

/**
 * <p>
 * Simple ACL implementation that keeps the entries in a Map whose keys are the
 * identities of the entries, to provide fast access.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
@Entity
@Table(name = "ACL")
public class ACLImpl implements ACL, Serializable {
	private static final long serialVersionUID = -6390609071167528812L;

	@Id
	@GeneratedValue
	private long aclID;

	@Transient
	private Resource resource;

	@Column(name = "resource")
	private String resourceAsString;

	@Transient
	private Map<String, ACLEntry> entriesMap;

	@OneToMany(mappedBy = "acl", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE,
			CascadeType.PERSIST }, orphanRemoval = true)
	private Collection<ACLEntryImpl> entries;

	/**
	 * <p>
	 * Builds an instance of {@code ACLImpl}. This constructor is required by
	 * the JPA specification.
	 * </p>
	 */
	ACLImpl() {
	}

	/**
	 * <p>
	 * Builds an instance of {@code ACLImpl} for the specified resource.
	 * </p>
	 * 
	 * @param resource
	 *            a reference to the {@code Resource} associated with the ACL
	 *            being constructed.
	 */
	public ACLImpl(Resource resource) {
		this(resource, new ArrayList<ACLEntry>());
	}

	/**
	 * <p>
	 * Builds an instance of {@code ACLImpl} for the specified resource, and
	 * initialize it with the specified entries.
	 * </p>
	 * 
	 * @param resource
	 *            a reference to the {@code Resource} associated with the ACL
	 *            being constructed.
	 * @param entries
	 *            a {@code Collection} containing the ACL's initial entries.
	 */
	public ACLImpl(Resource resource, Collection<ACLEntry> entries) {
		this(Util.getResourceAsString(resource), entries);
		this.resource = resource;
	}

	public ACLImpl(String resourceString, Collection<ACLEntry> entries) {
		this.resourceAsString = resourceString;
		this.entries = new ArrayList<ACLEntryImpl>();
		if (entries != null) {
			for (ACLEntry entry : entries) {
				ACLEntryImpl entryImpl = (ACLEntryImpl) entry;
				entryImpl.setAcl(this);
				this.entries.add(entryImpl);
			}
		}
		this.initEntriesMap();
	}

	/**
	 * <p>
	 * Obtains the persistent id of this {@code ACLImpl}.
	 * </p>
	 * 
	 * @return a {@code long} representing the persistent id this ACL.
	 */
	public long getACLId() {
		return this.aclID;
	}

	/**
	 * <p>
	 * Adds an entry to this ACL. If the ACL already has an {@code ACLEntry}
	 * associated to the new entry's identity, then the new entry will not be
	 * added.
	 * </p>
	 * 
	 * @param entry
	 *            the {@code ACLEntry} to be added.
	 * @return {@code true} if the entry was added; {@code false} otherwise.
	 */
	public boolean addEntry(ACLEntry entry) {
		if (this.entriesMap == null)
			this.initEntriesMap();

		// don't add a null entry or an entry that already existSELECT * FROM
		// ACL_ENTRYs.
		if (entry == null || this.entriesMap.get(entry.getIdentityOrRole()) != null)
			return false;
		this.entries.add((ACLEntryImpl) entry);
		((ACLEntryImpl) entry).setAcl(this);
		this.entriesMap.put(entry.getIdentityOrRole(), entry);
		return true;
	}

	/**
	 * @param entry
	 *            the entry to remove
	 * @return true if the remove operation has success
	 */
	public boolean removeEntry(ACLEntry entry) {
		if (this.entriesMap == null)
			this.initEntriesMap();
		this.entriesMap.remove(entry.getIdentityOrRole());
		return this.entries.remove(entry);
	}

	/**
	 * @return the list of the entries of the ACL
	 */
	public Collection<? extends ACLEntry> getEntries() {
		if (this.entriesMap == null)
			this.initEntriesMap();
		return Collections.unmodifiableCollection(this.entries);
	}

	/**
	 * @param identity
	 *            the identity
	 * @return th eentry for the identity
	 */
	public ACLEntry getEntry(Identity identity) {
		if (this.entriesMap == null)
			this.initEntriesMap();
		return this.entriesMap.get(identity.getName());
	}

	/**
	 * @param identityOrRole
	 *            the identity or role
	 * @return the referred entry for the role or identity
	 */
	public ACLEntry getEntry(String identityOrRole) {
		if (this.entriesMap == null)
			this.initEntriesMap();
		return this.entriesMap.get(identityOrRole);
	}

	/**
	 * @param permission
	 *            the permission to check
	 * @param identity
	 *            the identity to check
	 * @return true if it is granted
	 */
	public boolean isGranted(ACLPermission permission, Identity identity) {
		if (this.entriesMap == null)
			this.initEntriesMap();

		// lookup the entry corresponding to the specified identity.
		ACLEntry entry = this.entriesMap.get(identity.getName());
		if (entry != null) {
			// check the permission associated with the identity.
			return entry.checkPermission(permission);
		}
		return false;
	}

	/**
	 * <p>
	 * Obtains the stringfied representation of the resource associated with
	 * this {@code ACL}.
	 * </p>
	 * 
	 * @return a {@code String} representation of the resource.
	 */
	public String getResourceAsString() {
		return this.resourceAsString;
	}

	/**
	 * @return the current resource to check
	 */
	public Resource getResource() {
		return this.resource;
	}

	/**
	 * <p>
	 * Sets the resource associated with this {@code ACL}.
	 * </p>
	 * 
	 * @param resource
	 *            a reference to the {@code Resource} associated with this
	 *            {@code ACL}.
	 */
	public void setResource(Resource resource) {
		if (this.resource != null)
			throw MESSAGES.aclResourceAlreadySet();
		this.resource = resource;
	}

	/**
	 * <p>
	 * Initializes the entries map of this {@code ACL} instance.
	 * </p>
	 */
	private void initEntriesMap() {
		this.entriesMap = new HashMap<String, ACLEntry>();
		for (ACLEntry entry : this.entries)
			this.entriesMap.put(entry.getIdentityOrRole(), entry);
	}

}
