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
package it.vige.rubia.ui;

import static it.vige.rubia.ui.Constants.ERROR;
import static java.lang.Thread.currentThread;
import static java.util.ResourceBundle.getBundle;
import static javax.faces.context.FacesContext.getCurrentInstance;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * 
 * 
 */
public class JSFUtil {

	/**
	 *
	 * @author sshah
	 *
	 * @param name
	 *            the name to find in the request
	 *
	 * @return the found parameter of the request
	 *
	 */
	public static String getRequestParameter(String name) {
		String parameter = null;

		Map<String, String> requestParameterMap = getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (requestParameterMap != null) {
			parameter = requestParameterMap.get(name);
		}

		return parameter;
	}

	/**
	 * @return true if the application is inside a portal
	 *
	 */
	public static boolean isRunningInPortal() {
		boolean isRunningInPortal = false;
		return isRunningInPortal;
	}

	/**
	 * @return the context path of the application
	 */
	public static String getContextPath() {
		String contextPath = "";

		contextPath = getCurrentInstance().getExternalContext().getRequestContextPath();

		return contextPath;
	}

	/**
	 * @return true if the user is not logged
	 */
	public static boolean isAnonymous() {
		boolean anonymous = true;

		String remoteUser = getCurrentInstance().getExternalContext().getRemoteUser();
		if (remoteUser != null && remoteUser.trim().length() > 0) {
			anonymous = false;
		}

		return anonymous;
	}

	/**
	 *
	 * @author sshah
	 *
	 * @param componentId
	 *            the id component to check
	 *
	 * @return the value of the choosen component
	 *
	 */
	public static String getComponentValue(String componentId) {
		String value = null;

		UIViewRoot root = getCurrentInstance().getViewRoot();
		UIComponent component = root.findComponent(componentId);

		if (component != null) {
			Object o = component.getValueExpression("value").getValue(getCurrentInstance().getELContext());
			value = (String) o;
		}

		return value;
	}

	/**
	 *
	 * @author sshah
	 *
	 * @param componentId
	 *            the id component to check
	 *
	 */
	public static void removeComponent(String componentId) {
		UIViewRoot root = getCurrentInstance().getViewRoot();
		UIComponent component = root.findComponent(componentId);

		if (component != null) {
			UIComponent parent = component.getParent();
			parent.getChildren().remove(component);
		}
	}

	/**
	 *
	 * @author sshah
	 *
	 * @param e
	 *            the exception to handle
	 *
	 * @return the navigation state of the application
	 *
	 */
	public static String handleException(Exception e) {
		String genericNavState = ERROR;
		String msg = e.toString();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, // severity
				msg, // summary
				msg// detail
		);
		getCurrentInstance().addMessage(ERROR, message);

		return genericNavState;
	}

	/**
	 *
	 * @return the current error message
	 */
	public static String getErrorMsg() {
		String errorMsg = null;

		Iterator<FacesMessage> msgs = getCurrentInstance().getMessages(ERROR);
		if (msgs != null) {
			if (msgs.hasNext()) {
				FacesMessage message = msgs.next();
				errorMsg = message.getDetail();
			}
		}

		return errorMsg;
	}

	/**
	 * @return true if there is an error in the application context
	 */
	public static boolean isErrorOccurred() {
		boolean errorOccurred = false;

		Iterator<FacesMessage> msgs = getCurrentInstance().getMessages(ERROR);
		if (msgs != null && msgs.hasNext()) {
			errorOccurred = true;
		}

		return errorOccurred;
	}

	/**
	 * @param id
	 *            the id of the message to create
	 * @param msg
	 *            the text of the message to create
	 */
	public static void setMessage(String id, String msg) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, // severity
				msg, // summary
				msg// detail
		);
		getCurrentInstance().addMessage(id, message);
	}

	/**
	 * @param id
	 *            the id of the error message to create
	 * @param msg
	 *            the text of the error message to create
	 */
	public static void setErrorMessage(String id, String msg) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, // severity
				msg, // summary
				msg// detail
		);
		getCurrentInstance().addMessage(id, message);
	}

	/**
	 * @param id
	 *            the id of the message to find
	 *
	 * @return the text of the message
	 */
	public static String getMessage(String id) {
		String msg = null;

		Iterator<FacesMessage> msgs = getCurrentInstance().getMessages(id);
		if (msgs != null) {
			if (msgs.hasNext()) {
				FacesMessage message = msgs.next();
				msg = message.getDetail();
			}
		}

		return msg;
	}

	/**
	 * @param bundleName
	 *            the name of the bundle
	 * @param messageKey
	 *            the key of the bundle to find
	 *
	 * @return the value for the requested id
	 */
	public static String getBundleMessage(String bundleName, String messageKey) {
		String bundleMessage = null;

		// Getting ResourceBundle with current Locale
		FacesContext ctx = getCurrentInstance();
		UIViewRoot uiRoot = ctx.getViewRoot();
		Locale locale = uiRoot.getLocale();
		ClassLoader ldr = currentThread().getContextClassLoader();
		ResourceBundle bundle = getBundle(bundleName, locale, ldr);

		bundleMessage = bundle.getString(messageKey);

		return bundleMessage;
	}

	/**
	 * @return the current localization of the application
	 */
	public static Locale getSelectedLocale() {
		return getCurrentInstance().getExternalContext().getRequestLocale();
	}

	/**
	 * @return the default localization of the application
	 */
	public static Locale getDefaultLocale() {
		return getCurrentInstance().getApplication().getDefaultLocale();
	}

	/**
	 * @return the list of supported localizations in the application
	 */
	public static Iterator<Locale> getSupportedLocales() {
		return getCurrentInstance().getApplication().getSupportedLocales();
	}
}
