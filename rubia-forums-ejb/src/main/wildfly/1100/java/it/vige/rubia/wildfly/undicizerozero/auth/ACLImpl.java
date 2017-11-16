/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package it.vige.rubia.wildfly.undicizerozero.auth;

import static java.util.Collections.unmodifiableCollection;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.EAGER;
import static org.jboss.security.PicketBoxMessages.MESSAGES;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
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

	@OneToMany(mappedBy = "acl", fetch = EAGER, cascade = { REMOVE, PERSIST }, orphanRemoval = true)
	private Collection<ACLEntryImpl> entries;

	/**
	 * <p>
	 * Builds an instance of {@code ACLImpl}. This constructor is required by the
	 * JPA specification.
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
	 *            a reference to the {@code Resource} associated with the ACL being
	 *            constructed.
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
	 *            a reference to the {@code Resource} associated with the ACL being
	 *            constructed.
	 * @param entries
	 *            a {@code Collection} containing the ACL's initial entries.
	 */
	public ACLImpl(Resource resource, Collection<ACLEntry> entries) {
		this(Util.getResourceAsString(resource), entries);
		this.resource = resource;
	}

	public ACLImpl(String resourceString, Collection<ACLEntry> entries) {
		resourceAsString = resourceString;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.security.acl.ACL#addEntry(org.jboss.security.acl.ACLEntry)
	 */
	@Override
	public boolean addEntry(ACLEntry entry) {
		if (entriesMap == null)
			initEntriesMap();

		// don't add a null entry or an entry that already existSELECT * FROM
		// ACL_ENTRYs.
		if (entry == null || entriesMap.get(entry.getIdentityOrRole()) != null)
			return false;
		entries.add((ACLEntryImpl) entry);
		((ACLEntryImpl) entry).setAcl(this);
		entriesMap.put(entry.getIdentityOrRole(), entry);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.security.acl.ACL#removeEntry(org.jboss.security.acl.ACLEntry)
	 */
	@Override
	public boolean removeEntry(ACLEntry entry) {
		if (entriesMap == null)
			initEntriesMap();
		entriesMap.remove(entry.getIdentityOrRole());
		return entries.remove(entry);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.security.acl.ACL#getEntries()
	 */
	@Override
	public Collection<? extends ACLEntry> getEntries() {
		if (entriesMap == null)
			initEntriesMap();
		return unmodifiableCollection(this.entries);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.security.acl.ACL#getEntry(org.jboss.security.identity.Identity)
	 */
	@Override
	public ACLEntry getEntry(Identity identity) {
		if (entriesMap == null)
			initEntriesMap();
		return entriesMap.get(identity.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.security.acl.ACL#getEntry(java.lang.String)
	 */
	@Override
	public ACLEntry getEntry(String identityOrRole) {
		if (entriesMap == null)
			initEntriesMap();
		return entriesMap.get(identityOrRole);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.security.acl.ACL#isGranted(org.jboss.security.acl.ACLPermission,
	 * org.jboss.security.identity.Identity)
	 */
	@Override
	public boolean isGranted(ACLPermission permission, Identity identity) {
		if (entriesMap == null)
			initEntriesMap();

		// lookup the entry corresponding to the specified identity.
		ACLEntry entry = entriesMap.get(identity.getName());
		if (entry != null) {
			// check the permission associated with the identity.
			return entry.checkPermission(permission);
		}
		return false;
	}

	/**
	 * <p>
	 * Obtains the stringfied representation of the resource associated with this
	 * {@code ACL}.
	 * </p>
	 * 
	 * @return a {@code String} representation of the resource.
	 */
	public String getResourceAsString() {
		return this.resourceAsString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.security.acl.ACL#getResource()
	 */
	@Override
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
		if (resource != null)
			throw MESSAGES.aclResourceAlreadySet();
		this.resource = resource;
	}

	/**
	 * <p>
	 * Initializes the entries map of this {@code ACL} instance.
	 * </p>
	 */
	private void initEntriesMap() {
		entriesMap = new HashMap<String, ACLEntry>();
		for (ACLEntry entry : entries)
			entriesMap.put(entry.getIdentityOrRole(), entry);
	}

}
