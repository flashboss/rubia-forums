package it.vige.rubia.properties;

import static java.util.ResourceBundle.getBundle;

public enum NotificationType {
	EMAIL_LINKED_NOTIFICATION, EMAIL_EMBEDED_NOTIFICATION, EMAIL_NO_NOTIFICATION;

	@Override
	public String toString() {
		return getBundle("ResourceJSF").getString(name());
	}
}
