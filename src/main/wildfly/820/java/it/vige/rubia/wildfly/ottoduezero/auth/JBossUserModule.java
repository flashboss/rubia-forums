package it.vige.rubia.wildfly.ottoduezero.auth;

import static it.vige.rubia.ui.JSFUtil.isAnonymous;
import it.vige.rubia.auth.User;
import it.vige.rubia.auth.UserModule;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.inject.Named;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.query.AttributeParameter;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.QueryParameter;

@Named("userModule")
@Stateful
public class JBossUserModule implements UserModule, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8560321558665446098L;
	@Inject
	private IdentityManager identityManager;

	@Override
	public User findUserByUserName(String arg0) throws IllegalArgumentException {
		User user = null;
		try {
			org.picketlink.idm.model.basic.User newUser = getUser(arg0);
			user = new JBossUser(newUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User findUserById(String arg0) throws IllegalArgumentException {
		User user = null;
		try {
			org.picketlink.idm.model.basic.User newUser = getUser(arg0);
			user = new JBossUser(newUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public boolean isGuest() {
		// TODO Auto-generated method stub
		return isAnonymous();
	}

	private org.picketlink.idm.model.basic.User getUser(String userId) {
		IdentityQuery<org.picketlink.idm.model.basic.User> query = identityManager
				.createIdentityQuery(org.picketlink.idm.model.basic.User.class);
		QueryParameter id = new AttributeParameter("id");
		query.setParameter(id, userId);
		org.picketlink.idm.model.basic.User newUser = query.getResultList()
				.get(0);
		return newUser;
	}
}
