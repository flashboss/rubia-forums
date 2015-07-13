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
package it.vige.rubia.ui.action.validators;

import static it.vige.rubia.Constants.BUNDLE_NAME;
import static it.vige.rubia.ui.JSFUtil.getBundleMessage;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.context.FacesContext.getCurrentInstance;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Abstract class for defining validators that check length of user input in a
 * JSF UIComponent.
 * 
 * @author <a href="ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * 
 */
abstract class LengthValidator implements Validator {

	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		// A check whether it is post submition or preview action. If not
		// validators are not executed.
		FacesContext fc = getCurrentInstance();
		Map<String, String> reqParams = fc.getExternalContext().getRequestParameterMap();
		if (!(reqParams.keySet().contains("post:Preview") || reqParams.keySet().contains("post:Submit"))) {
			return;
		}

		UIComponent formComp = component.getParent();
		UIComponent validatedComp = getComponentToValidation(formComp);

		if (validatedComp.getAttributes().get("value") == null
				|| validatedComp.getAttributes().get("value").toString().trim().length() < 1) {
			FacesMessage message = new FacesMessage();
			message.setDetail(getBundleMessage(BUNDLE_NAME, getMessage()));
			message.setSummary(getBundleMessage(BUNDLE_NAME, getMessage()));
			message.setSeverity(SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
	}

	protected abstract String getMessage();

	protected abstract UIComponent getComponentToValidation(UIComponent parentComponent);

}
