package org.vige.rubia.jbossas.setteunouno.auth;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ACLImpl.class)
public abstract class ACLImpl_ {

	public static volatile SingularAttribute<ACLImpl, String> resourceAsString;
	public static volatile CollectionAttribute<ACLImpl, ACLEntryImpl> entries;
	public static volatile SingularAttribute<ACLImpl, Long> aclID;

}

