package org.vige.rubia.web.auth;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.vige.rubia.eap.seiunozero.auth.ACLEntryImpl;
import org.vige.rubia.eap.seiunozero.auth.ACLImpl;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ACLEntryImpl.class)
public abstract class ACLEntryImpl_ {

	public static volatile SingularAttribute<ACLEntryImpl, ACLImpl> acl;
	public static volatile SingularAttribute<ACLEntryImpl, String> identityOrRole;
	public static volatile SingularAttribute<ACLEntryImpl, Long> entryID;
	public static volatile SingularAttribute<ACLEntryImpl, Integer> bitMask;

}

