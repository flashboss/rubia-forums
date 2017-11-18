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

import static it.vige.rubia.Constants.DEFAULT_DATE_PATTERN;
import static it.vige.rubia.format.render.bbcodehtml.ToHTMLConfig.FILTER_MODE_ALWAYS_PRINT;
import static it.vige.rubia.format.render.bbcodehtml.ToHTMLConfig.FILTER_MODE_NEVER_PRINT;
import static it.vige.rubia.format.render.bbcodehtml.ToHTMLConfig.OUTPUT_MODE_REMOVE;
import static it.vige.rubia.ui.JSFUtil.getRequestParameter;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Thread.currentThread;
import static java.util.Locale.getDefault;
import static java.util.Locale.Category.DISPLAY;
import static java.util.ResourceBundle.getBundle;
import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import it.vige.rubia.format.render.bbcodehtml.ToHTMLConfig;
import it.vige.rubia.format.render.bbcodehtml.ToHTMLRenderer;

/**
 * @author <a href="mailto:sohil.shah@jboss.com">Sohil Shah</a>
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 */
public class ForumUtil {

	public final static String TRUNCATE = "...";

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

	/**
	 * Get a <code>SimpleDateFormat</code> object from the session. The object is
	 * stored in the session because it is expensive to create and we want to reuse
	 * it as much as we can. Also it is configured with the date format taken from
	 * the preference of the user if it exists.
	 * 
	 * @return the format object
	 */
	public static SimpleDateFormat getSDF() {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_PATTERN, getDefault(DISPLAY));
		return sdf;
	}

	/**
	 * Method used for parsing bbcode and return properly formated text of message.
	 * 
	 * @param text
	 *            the text of the message
	 * @param allowHTML
	 *            if true, the message will be formatted in html
	 * @return the formatted message
	 */
	public static String formatMessage(String text, boolean allowHTML) {

		try {
			Object req = getCurrentInstance().getExternalContext().getRequest();

			if (allowHTML) {
				getToHTMLRenderer(req).getConfig().setFilterMode(FILTER_MODE_ALWAYS_PRINT);
				getToHTMLRenderer(req).getConfig().setOuputMode(OUTPUT_MODE_REMOVE);
				getToHTMLRenderer(req).getConfig().setMaxTextWidth(MAX_VALUE);
			} else {
				getToHTMLRenderer(req).getConfig().setFilterMode(FILTER_MODE_NEVER_PRINT);
				getToHTMLRenderer(req).getConfig().setOuputMode(OUTPUT_MODE_REMOVE);
				getToHTMLRenderer(req).getConfig().setMaxTextWidth(MAX_VALUE);
			}
			return formatTitle(req, text);
		} catch (Exception e) {
			// Now if something goes wrong it just returns message with bbcode.
			return text;
		}

	}

	/**
	 * @param req
	 *            the request that maybe contains the format object
	 * @return the renderer for the requested request
	 */
	private static ToHTMLRenderer getToHTMLRenderer(Object req) {
		ToHTMLRenderer renderer = null;
		// TODO: GETTING RENDERER FROM APPLICATION SCOPE ATTRIBUTE
		if (renderer == null) {

			// Getting ResourceBundle with current Locale
			FacesContext ctx = getCurrentInstance();
			UIViewRoot uiRoot = ctx.getViewRoot();
			Locale locale = uiRoot.getLocale();
			ClassLoader ldr = currentThread().getContextClassLoader();
			ResourceBundle bundle = getBundle("ResourceJSF", locale, ldr);

			// Create the HTMLRenderer for BBCode
			ToHTMLConfig config = new ToHTMLConfig();
			renderer = new ToHTMLRenderer(config, bundle);
		}
		return renderer;
	}

	/**
	 * @param req
	 *            the request that maybe contains the format object
	 * @param text
	 *            the text to render
	 * @return the formatted text
	 */
	public static String formatTitle(Object req, String text) {

		StringWriter stringWriter = new StringWriter();
		getToHTMLRenderer(req).render(text.toCharArray(), 0, text.length());
		getToHTMLRenderer(req).getConfig().setMaxTextWidth(MAX_VALUE);
		return stringWriter.toString();

	}

	public static String truncate(String message, int length) {
		if (message != null) {
			if (message.length() >= length)
				return message.substring(0, length) + TRUNCATE;
		}
		return message;
	}

}
