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

import static it.vige.rubia.ui.action.validators.ValidatorMessages.MESSAGE_LENGTH_ERROR;

import javax.faces.component.UIComponent;
import javax.faces.validator.FacesValidator;

/**
 * Validator class for validating a length of user input in post message.
 * 
 * @author <a href="ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * 
 */
@FacesValidator("messageLengthValidator")
public class MessageLengthValidator extends LengthValidator {

	protected String getMessage() {
		return MESSAGE_LENGTH_ERROR;
	}

	protected UIComponent getComponentToValidation(UIComponent parentComponent) {
		return parentComponent.findComponent("message");
	}

}
