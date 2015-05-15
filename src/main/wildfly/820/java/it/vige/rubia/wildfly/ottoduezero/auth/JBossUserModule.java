package it.vige.rubia.wildfly.ottoduezero.auth;

import static it.vige.rubia.ui.JSFUtil.isAnonymous;
import it.vige.rubia.auth.User;
import it.vige.rubia.auth.UserModule;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Named;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.internal.DefaultPartitionManager;
import org.picketlink.idm.model.basic.Realm;
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
	private IdentityManager identityManager;

	@Override
	public User findUserByUserName(String arg0) throws IllegalArgumentException {
		loadIdentityManager();
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
		loadIdentityManager();
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
		QueryParameter id = new AttributeParameter("loginName");
		query.setParameter(id, userId);
		List<org.picketlink.idm.model.basic.User> newUsers = query
				.getResultList();
		if (newUsers.size() > 0)
			return newUsers.get(0);
		else
			return null;
	}

	private void loadIdentityManager() {
		if (identityManager == null) {
			IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();
			builder.named("file-store-preserve-state").stores().file()
					.preserveState(true).supportAllFeatures();
			PartitionManager partitionManager = new DefaultPartitionManager(
					builder.buildAll());
			Realm realm = partitionManager.getPartition(Realm.class,
					"forums-realm");
			if (realm == null) {
				realm = new Realm("forums-realm");
				partitionManager.add(realm);
			}
			identityManager = partitionManager.createIdentityManager(realm);
			insertUser("root");
			insertUser("mary");
			insertUser("john");
			insertUser("demo");
		}
	}

	private void insertUser(String loginName) {
		org.picketlink.idm.model.basic.User user = getUser(loginName);
		if (user == null) {
			user = new org.picketlink.idm.model.basic.User(loginName);
			identityManager.add(user);
		}

	}
}
