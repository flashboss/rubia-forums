package it.vige.rubia.wildfly.ottoduezero.auth;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.picketlink.idm.config.AbstractIdentityStoreConfiguration;
import org.picketlink.idm.credential.AbstractBaseCredentials;
import org.picketlink.idm.credential.handler.CredentialHandler;
import org.picketlink.idm.internal.AbstractIdentityStore;
import org.picketlink.idm.model.AttributedType;
import org.picketlink.idm.spi.ContextInitializer;

public class ForumsIdentityStoreConfiguration extends
		AbstractIdentityStoreConfiguration {
	public ForumsIdentityStoreConfiguration() {
		this(null, null, null, null, null, true, true, true);
	}

	protected ForumsIdentityStoreConfiguration(
			Map<Class<? extends AttributedType>, Set<IdentityOperation>> supportedTypes,
			Map<Class<? extends AttributedType>, Set<IdentityOperation>> unsupportedTypes,
			List<ContextInitializer> contextInitializers,
			Map<String, Object> credentialHandlerProperties,
			Set<Class<? extends CredentialHandler<AbstractIdentityStore<ForumsIdentityStoreConfiguration>, AbstractBaseCredentials, Object>>> credentialHandlers,
			boolean supportsAttribute, boolean supportsCredential,
			boolean supportsPermissions) {
		super(supportedTypes, unsupportedTypes, contextInitializers,
				credentialHandlerProperties, null, supportsAttribute,
				supportsCredential, supportsPermissions);
	}

}
