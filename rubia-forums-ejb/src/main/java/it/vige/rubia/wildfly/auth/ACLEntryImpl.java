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
package it.vige.rubia.wildfly.auth;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.jboss.security.PicketBoxMessages;
import org.jboss.security.acl.ACLEntry;
import org.jboss.security.acl.ACLPermission;
import org.jboss.security.acl.BitMaskPermission;
import org.jboss.security.acl.CompositeACLPermission;
import org.jboss.security.identity.Identity;

/**
 * <p>
 * This class represents an entry in the Access Control List (ACL), and
 * associates a permission to an identity. This implementation only stores
 * permissions of type {@code BitMaskPermission}, and can also only check
 * permissions of that type.
 * </p>
 * 
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
@Entity
@Table(name = "ACL_ENTRY")
public class ACLEntryImpl implements ACLEntry, Serializable {
	private static final long serialVersionUID = -2985214023383451768L;

	@Id
	@GeneratedValue
	private long entryID;

	@Transient
	private BitMaskPermission permission;

	/* persist only the bitmask */
	private int bitMask;

	@Transient
	private Identity identity;

	/* persist the string representation of the identity or role */
	private String identityOrRole;

	@ManyToOne
	private ACLImpl acl;

	/**
	 * <p>
	 * Builds an instance of {@code ACLEntryImpl}. This constructor is required by
	 * the JPA specification.
	 * </p>
	 */
	ACLEntryImpl() {
	}

	/**
	 * <p>
	 * Builds an instance of {@code ACLEntryImpl} with the specified permission and
	 * identity.
	 * </p>
	 * 
	 * @param permission the {@code ACLPermission} granted to the associated
	 *                   identity.
	 * @param identity   the {@code Identity} for which the permission is being
	 *                   granted.
	 */
	public ACLEntryImpl(BitMaskPermission permission, Identity identity) {
		this.permission = permission;
		this.identity = identity;
		this.identityOrRole = identity.getName();
	}

	/**
	 * <p>
	 * Builds an instance of {@code ACLEntryImpl} with the specified permission and
	 * identity/role name.
	 * </p>
	 * 
	 * @param permission     the {@code ACLPermission} granted to the associated
	 *                       identity.
	 * @param identityOrRole a {@code String} representing the identity or role
	 *                       name.
	 */
	public ACLEntryImpl(BitMaskPermission permission, String identityOrRole) {
		this.permission = permission;
		this.identityOrRole = identityOrRole;
	}

	/**
	 * <p>
	 * Obtains the persistent id of this {@code ACLEntryImpl}.
	 * </p>
	 * 
	 * @return a {@code long} representing the persistent id this entry.
	 */
	public long getACLEntryId() {
		return this.entryID;
	}

	/**
	 * <p>
	 * Method called by the JPA layer before persisting the fields.
	 * </p>
	 */
	@PrePersist
	private void setPersistentFields() {
		if (this.permission != null)
			this.bitMask = this.permission.getMaskValue();
	}

	/**
	 * <p>
	 * Method called by the JPA layer after loading the persisted object.
	 * </p>
	 */
	@PostLoad
	private void loadState() {
		if (this.permission != null)
			throw PicketBoxMessages.MESSAGES.aclEntryPermissionAlreadySet();
		this.permission = new CompositeACLPermission(this.bitMask);
	}

	public ACLImpl getAcl() {
		return this.acl;
	}

	public void setAcl(ACLImpl acl) {
		this.acl = acl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.security.acl.ACLEntry#getIdentityOrRole()
	 */
	public String getIdentityOrRole() {
		return this.identityOrRole;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.security.acl.ACLEntry#getIdentity()
	 */
	public Identity getIdentity() {
		return this.identity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.security.acl.ACLEntry#getPermission()
	 */
	public ACLPermission getPermission() {
		return this.permission;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.security.acl.ACLEntry#checkPermission(org.jboss.security.acl.
	 * ACLPermission)
	 */
	public boolean checkPermission(ACLPermission permission) {
		if (!(permission instanceof BitMaskPermission))
			return false;
		BitMaskPermission bitmaskPermission = (BitMaskPermission) permission;
		// an empty permission is always part of another permission.
		if (bitmaskPermission.getMaskValue() == 0)
			return true;
		// simple implementation: if all bits match, return true.
		return (this.permission.getMaskValue() & bitmaskPermission.getMaskValue()) == bitmaskPermission.getMaskValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ACLEntryImpl) {
			ACLEntryImpl entry = (ACLEntryImpl) obj;
			return this.identityOrRole.equals(entry.identityOrRole);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.identityOrRole.hashCode();
	}
}
