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
import static it.vige.rubia.ui.action.validators.ValidatorMessages.EMPTY_POLL_OPTION_MSG;
import static it.vige.rubia.ui.action.validators.ValidatorMessages.EMPTY_POLL_QUESTION_MSG;
import static it.vige.rubia.ui.action.validators.ValidatorMessages.POLL_DURATION_MSG;
import static it.vige.rubia.ui.action.validators.ValidatorMessages.TOO_FEW_OPTIONS_MSG;
import static it.vige.rubia.ui.action.validators.ValidatorMessages.TOO_MANY_OPTIONS_MSG;
import static java.lang.Integer.parseInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

/**
 * PollValidator is a class that implements JSF Validator interface and is used
 * for validating a forum poll.
 * 
 * @author <a href="ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * 
 */
@FacesValidator("pollValidator")
public class PollValidator implements Validator<String> {

	public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {

		// A check whether it is post submition action. If not validators are
		// not executed.
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String, String> reqParams = fc.getExternalContext().getRequestParameterMap();
		if (!reqParams.keySet().contains("post:Submit")) {
			return;
		}

		// Collecting all poll data from the form.
		UIComponent formComp = component.getParent();
		UIComponent questionComp = formComp.findComponent("question");
		UIComponent pollDurationComp = formComp.findComponent("pollDuration");
		List<UIComponent> options = new ArrayList<UIComponent>();
		for (int i = 1;; i++) {
			UIComponent temp = formComp.findComponent("option_" + i);
			if (temp != null) {
				options.add(temp);
			} else {
				break;
			}
		}

		// If there are no question or options provided by the user, we don't
		// validate.
		if (!((questionComp.getAttributes().get("value") != null
				&& questionComp.getAttributes().get("value").toString().trim().length() != 0)
				|| (options.size() > 0))) {
			return;
		}

		// Checks
		if (options.size() > 10) {
			throwValidationException(TOO_MANY_OPTIONS_MSG);
		}
		if (options.size() < 2) {
			throwValidationException(TOO_FEW_OPTIONS_MSG);
		}
		if (questionComp.getAttributes().get("value") == null
				|| questionComp.getAttributes().get("value").toString().trim().length() == 0) {
			throwValidationException(EMPTY_POLL_QUESTION_MSG);
		}
		for (UIComponent option : options) {
			if (option.getAttributes().get("value") == null
					|| option.getAttributes().get("value").toString().trim().length() == 0) {
				throwValidationException(EMPTY_POLL_OPTION_MSG);
			}
		}

		int duration = 0;
		if (pollDurationComp.getAttributes().get("value") != null
				&& pollDurationComp.getAttributes().get("value").toString().trim().length() != 0) {
			try {
				duration = parseInt(pollDurationComp.getAttributes().get("value").toString());
			} catch (NumberFormatException e) {
				throwValidationException(POLL_DURATION_MSG);
			}
			if (duration < 0) {
				throwValidationException(POLL_DURATION_MSG);
			}
		}
	}

	private void throwValidationException(String exceptionMsg) throws ValidatorException {
		FacesMessage message = new FacesMessage();
		message.setDetail(getBundleMessage(BUNDLE_NAME, exceptionMsg));
		message.setSummary(getBundleMessage(BUNDLE_NAME, exceptionMsg));
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		throw new ValidatorException(message);
	}
}
