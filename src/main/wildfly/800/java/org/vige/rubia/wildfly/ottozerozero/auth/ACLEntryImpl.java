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
package org.vige.rubia.wildfly.ottozerozero.auth;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.jboss.security.ErrorCodes;
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
	 * Builds an instance of {@code ACLEntryImpl}. This constructor is required
	 * by the JPA specification.
	 * </p>
	 */
	ACLEntryImpl() {
	}

	/**
	 * <p>
	 * Builds an instance of {@code ACLEntryImpl} with the specified permission
	 * and identity.
	 * </p>
	 * 
	 * @param permission
	 *            the {@code ACLPermission} granted to the associated identity.
	 * @param identity
	 *            the {@code Identity} for which the permission is being
	 *            granted.
	 */
	public ACLEntryImpl(BitMaskPermission permission, Identity identity) {
		this.permission = permission;
		this.identity = identity;
		this.identityOrRole = identity.getName();
	}

	/**
	 * <p>
	 * Builds an instance of {@code ACLEntryImpl} with the specified permission
	 * and identity/role name.
	 * </p>
	 * 
	 * @param permission
	 *            the {@code ACLPermission} granted to the associated identity.
	 * @param identityOrRole
	 *            a {@code String} representing the identity or role name.
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
			throw new IllegalStateException(ErrorCodes.PROCESSING_FAILED
					+ "ACLEntry permission has already been set");
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
	 * @see
	 * org.jboss.security.acl.ACLEntry#checkPermission(org.jboss.security.acl
	 * .ACLPermission)
	 */
	public boolean checkPermission(ACLPermission permission) {
		if (!(permission instanceof BitMaskPermission))
			return false;
		BitMaskPermission bitmaskPermission = (BitMaskPermission) permission;
		// an empty permission is always part of another permission.
		if (bitmaskPermission.getMaskValue() == 0)
			return true;
		// simple implementation: if all bits match, return true.
		return (this.permission.getMaskValue() & bitmaskPermission
				.getMaskValue()) == bitmaskPermission.getMaskValue();
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
