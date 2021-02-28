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
package it.vige.rubia.ui.action.converters;

import static it.vige.rubia.Constants.BUNDLE_NAME;
import static it.vige.rubia.ui.JSFUtil.getBundleMessage;
import static it.vige.rubia.ui.action.validators.ValidatorMessages.POLL_DURATION_MSG;
import static java.lang.Integer.valueOf;
import static jakarta.faces.application.FacesMessage.SEVERITY_ERROR;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;

/**
 * This is a special JSF converter for poll duration time. It's more or less
 * String - Integer converter but with the option that empty String equals to 0.
 * 
 * @author <a href="ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 * 
 */
@FacesConverter("pollDurationConverter")
public class PollDurationConverter implements Converter<Integer> {

	public Integer getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {

		if (value == null || value.trim().length() == 0) {
			return 0;
		} else {
			try {
				return valueOf(value);
			} catch (NumberFormatException e) {
				FacesMessage message = new FacesMessage();
				message.setDetail(getBundleMessage(BUNDLE_NAME, POLL_DURATION_MSG));
				message.setSummary(getBundleMessage(BUNDLE_NAME, POLL_DURATION_MSG));
				message.setSeverity(SEVERITY_ERROR);
				throw new ConverterException(message);
			}
		}
	}

	public String getAsString(FacesContext context, UIComponent component, Integer value) throws ConverterException {
		if (value == null) {
			return "0";
		}
		return value.toString();
	}

}
