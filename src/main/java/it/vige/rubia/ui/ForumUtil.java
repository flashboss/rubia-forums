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

import static it.vige.rubia.ui.JSFUtil.getRequestParameter;
import static it.vige.rubia.ui.PortalUtil.getSDF;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
public class ForumUtil {
	/**
	 *
	 * @param param
	 *            the param to find
	 * @return the param of the request
	 */
	public static String getParameter(String param) {
		String parameter = null;

		parameter = getRequestParameter(param);

		return parameter;
	}

	/**
	 * @param input
	 *            the percent number to convert
	 * @return the converted percent number
	 *
	 */
	public static String getPercentStr(double input) {
		String percent = "";

		DecimalFormat decimalFormat = new DecimalFormat("##.##%");
		percent = decimalFormat.format(input);

		return percent;
	}

	public static String nullIfEmptyString(String string) {
		if (string == null || string.trim().length() == 0) {
			return null;
		}
		return string.trim();
	}

	public static boolean isFeedsConfigured() {
		return true;
	}

	/**
	 * @param date
	 *            the date to convert
	 *
	 * @return the converted date
	 *
	 */
	public static String getDateStr(Date date) {
		String dateStr = "";

		if (date != null) {
			dateStr = getSDF().format(date);
		}

		return dateStr;
	}

}
