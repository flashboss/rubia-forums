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
package org.vige.rubia.renderer;

import static org.richfaces.renderkit.RenderKitUtils.attributes;
import static org.richfaces.renderkit.RenderKitUtils.renderPassThroughAttributes;
import static org.richfaces.renderkit.RenderKitUtils.shouldRenderAttribute;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.renderkit.ControlsState;
import org.richfaces.renderkit.DataScrollerBaseRenderer;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RenderKitUtils.Attributes;

public class ForumsDataScrollerRenderer extends DataScrollerBaseRenderer {

	private static final Attributes PASS_THROUGH_ATTRIBUTES8 = attributes().generic("title", "title")

	;

	private static String convertToString(Object object) {
		return object != null ? object.toString() : "";
	}

	@Override
	public void doEncodeEnd(ResponseWriter responseWriter, FacesContext facesContext, UIComponent component) throws IOException {
		String clientId = component.getClientId(facesContext);
		ControlsState controlsState = (ControlsState) this.getControlsState(facesContext, component);
		Object style = (Object) component.getAttributes().get("style");
		Object styleClass = (Object) component.getAttributes().get("styleClass");
		responseWriter.startElement("span", component);
		{
			String value = convertToString(styleClass);
			if (null != value && value.length() > 0) {
				responseWriter.writeAttribute("class", value, null);
			}

		}

		{
			String value = clientId;
			if (null != value && value.length() > 0) {
				responseWriter.writeAttribute("id", value, null);
			}

		}

		{
			Object value = (this.shouldRender(component) ? style : "display:none");
			if (null != value && shouldRenderAttribute(value)) {
				responseWriter.writeAttribute("style", value, null);
			}

		}

		renderPassThroughAttributes(facesContext, component, PASS_THROUGH_ATTRIBUTES8);
		if (this.shouldRender(component)) {
			if (controlsState.getFirstRendered()) {
				boolean isEnabled = (boolean) controlsState.getFirstEnabled();
				UIComponent facet = (UIComponent) component.getFacet("first");
				String enabledStyles = "";
				String disabledStyles = "disablepage";
				String id = (String) convertToString(clientId) + "_ds_f";
				String defaultText = (String) "\u00AB\u00AB\u00AB\u00AB";
				if (isEnabled) {
					responseWriter.startElement("a", component);
					{
						String value = enabledStyles;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("class", value, null);
						}

					}

					responseWriter.writeURIAttribute("href", "javascript:void(0);", null);

					{
						String value = id;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("id", value, null);
						}

					}

					if ((facet != null)) {
						encodeFacet(facesContext, facet);
						;
					} else {
						{
							Object text = defaultText;
							if (text != null) {
								responseWriter.writeText(text, null);
							}
						}

					}
					responseWriter.endElement("a");
				} else {
					responseWriter.startElement("span", component);
					{
						String value = disabledStyles;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("class", value, null);
						}

					}

					{
						String value = id;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("id", value, null);
						}

					}

					if ((facet != null)) {
						encodeFacet(facesContext, facet);
						;
					} else {
						{
							Object text = defaultText;
							if (text != null) {
								responseWriter.writeText(text, null);
							}
						}

					}
					responseWriter.endElement("span");
				}
			}
			if (controlsState.getFastRewindRendered()) {
				boolean isEnabled = (boolean) controlsState.getFastRewindEnabled();
				UIComponent facet = (UIComponent) component.getFacet("fastRewind");
				String enabledStyles = "";
				String disabledStyles = "disablepage";
				String id = (String) convertToString(clientId) + "_ds_fr";
				String defaultText = (String) "\u00AB\u00AB";
				if (isEnabled) {
					responseWriter.startElement("a", component);
					{
						String value = enabledStyles;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("class", value, null);
						}

					}

					responseWriter.writeURIAttribute("href", "javascript:void(0);", null);

					{
						String value = id;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("id", value, null);
						}

					}

					if ((facet != null)) {
						encodeFacet(facesContext, facet);
						;
					} else {
						{
							Object text = defaultText;
							if (text != null) {
								responseWriter.writeText(text, null);
							}
						}

					}
					responseWriter.endElement("a");
				} else {
					responseWriter.startElement("span", component);
					{
						String value = disabledStyles;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("class", value, null);
						}

					}

					{
						String value = id;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("id", value, null);
						}

					}

					if ((facet != null)) {
						encodeFacet(facesContext, facet);
						;
					} else {
						{
							Object text = defaultText;
							if (text != null) {
								responseWriter.writeText(text, null);
							}
						}

					}
					responseWriter.endElement("span");
				}
			}
			if (controlsState.getPreviousRendered()) {
				boolean isEnabled = (boolean) controlsState.getPreviousEnabled();
				UIComponent facet = (UIComponent) component.getFacet("previous");
				String enabledStyles = "";
				String disabledStyles = "disablepage";
				String id = (String) convertToString(clientId) + "_ds_prev";
				String defaultText = (String) "\u00AB";
				if (isEnabled) {
					responseWriter.startElement("a", component);
					{
						String value = enabledStyles;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("class", value, null);
						}

					}

					responseWriter.writeURIAttribute("href", "javascript:void(0);", null);

					{
						String value = id;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("id", value, null);
						}

					}

					if ((facet != null)) {
						encodeFacet(facesContext, facet);
						;
					} else {
						{
							Object text = defaultText;
							if (text != null) {
								responseWriter.writeText(text, null);
							}
						}

					}
					responseWriter.endElement("a");
				} else {
					responseWriter.startElement("span", component);
					{
						String value = disabledStyles;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("class", value, null);
						}

					}

					{
						String value = id;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("id", value, null);
						}

					}

					if ((facet != null)) {
						encodeFacet(facesContext, facet);
						;
					} else {
						{
							Object text = defaultText;
							if (text != null) {
								responseWriter.writeText(text, null);
							}
						}

					}
					responseWriter.endElement("span");
				}
			}
			Map<String, String> digitals = (Map<String, String>) this.renderPager(responseWriter, facesContext, component);
			if (controlsState.getNextRendered()) {
				boolean isEnabled = (boolean) controlsState.getNextEnabled();
				UIComponent facet = (UIComponent) component.getFacet("next");
				String enabledStyles = "";
				String disabledStyles = "disablepage";
				String id = (String) convertToString(clientId) + "_ds_next";
				String defaultText = (String) "\u00BB";
				if (isEnabled) {
					responseWriter.startElement("a", component);
					{
						String value = enabledStyles;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("class", value, null);
						}

					}

					responseWriter.writeURIAttribute("href", "javascript:void(0);", null);

					{
						String value = id;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("id", value, null);
						}

					}

					if ((facet != null)) {
						encodeFacet(facesContext, facet);
						;
					} else {
						{
							Object text = defaultText;
							if (text != null) {
								responseWriter.writeText(text, null);
							}
						}

					}
					responseWriter.endElement("a");
				} else {
					responseWriter.startElement("span", component);
					{
						String value = disabledStyles;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("class", value, null);
						}

					}

					{
						String value = id;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("id", value, null);
						}

					}

					if ((facet != null)) {
						encodeFacet(facesContext, facet);
						;
					} else {
						{
							Object text = defaultText;
							if (text != null) {
								responseWriter.writeText(text, null);
							}
						}

					}
					responseWriter.endElement("span");
				}
			}
			if (controlsState.getFastForwardRendered()) {
				boolean isEnabled = (boolean) controlsState.getFastForwardEnabled();
				UIComponent facet = (UIComponent) component.getFacet("fastForward");
				String enabledStyles = "";
				String disabledStyles = "disablepage";
				String id = (String) convertToString(clientId) + "_ds_ff";
				String defaultText = (String) "\u00BB\u00BB";
				if (isEnabled) {
					responseWriter.startElement("a", component);
					{
						String value = enabledStyles;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("class", value, null);
						}

					}

					responseWriter.writeURIAttribute("href", "javascript:void(0);", null);

					{
						String value = id;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("id", value, null);
						}

					}

					if ((facet != null)) {
						encodeFacet(facesContext, facet);
						;
					} else {
						{
							Object text = defaultText;
							if (text != null) {
								responseWriter.writeText(text, null);
							}
						}

					}
					responseWriter.endElement("a");
				} else {
					responseWriter.startElement("span", component);
					{
						String value = disabledStyles;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("class", value, null);
						}

					}

					{
						String value = id;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("id", value, null);
						}

					}

					if ((facet != null)) {
						encodeFacet(facesContext, facet);
						;
					} else {
						{
							Object text = defaultText;
							if (text != null) {
								responseWriter.writeText(text, null);
							}
						}

					}
					responseWriter.endElement("span");
				}
			}
			if (controlsState.getLastRendered()) {
				boolean isEnabled = (boolean) controlsState.getLastEnabled();
				UIComponent facet = (UIComponent) component.getFacet("last");
				String enabledStyles = "";
				String disabledStyles = "disablepage";
				String id = (String) convertToString(clientId) + "_ds_l";
				String defaultText = (String) "\u00BB\u00BB\u00BB\u00BB";
				if (isEnabled) {
					responseWriter.startElement("a", component);
					{
						String value = enabledStyles;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("class", value, null);
						}

					}

					responseWriter.writeURIAttribute("href", "javascript:void(0);", null);

					{
						String value = id;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("id", value, null);
						}

					}

					if ((facet != null)) {
						encodeFacet(facesContext, facet);
						;
					} else {
						{
							Object text = defaultText;
							if (text != null) {
								responseWriter.writeText(text, null);
							}
						}

					}
					responseWriter.endElement("a");
				} else {
					responseWriter.startElement("span", component);
					{
						String value = disabledStyles;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("class", value, null);
						}

					}

					{
						String value = id;
						if (null != value && value.length() > 0) {
							responseWriter.writeAttribute("id", value, null);
						}

					}

					if ((facet != null)) {
						encodeFacet(facesContext, facet);
						;
					} else {
						{
							Object text = defaultText;
							if (text != null) {
								responseWriter.writeText(text, null);
							}
						}

					}
					responseWriter.endElement("span");
				}
			}
			Map<String, Map<String, String>> buttons = (Map<String, Map<String, String>>) this.getControls(facesContext, component, controlsState);
			responseWriter.startElement("script", component);
			responseWriter.writeAttribute("type", "text/javascript", null);

			buildScript(responseWriter, facesContext, component, buttons, digitals);
			;
			responseWriter.endElement("script");
		}
		responseWriter.endElement("span");

	}

	@Override
	public Map<String, String> renderPager(ResponseWriter out, FacesContext context, UIComponent component) throws IOException {

		int currentPage = (Integer) component.getAttributes().get("page");
		int maxPages = (Integer) component.getAttributes().get("maxPagesOrDefault");
		int pageCount = (Integer) component.getAttributes().get("pageCount");

		Map<String, String> digital = new HashMap<String, String>();

		if (pageCount <= 1) {
			return digital;
		}

		int delta = maxPages / 2;

		int pages;
		int start;

		if (pageCount > maxPages && currentPage > delta) {
			pages = maxPages;
			start = currentPage - pages / 2 - 1;
			if (start + pages > pageCount) {
				start = pageCount - pages;
			}
		} else {
			pages = pageCount < maxPages ? pageCount : maxPages;
			start = 0;
		}

		String clientId = component.getClientId(context);

		int size = start + pages;
		for (int i = start; i < size; i++) {

			boolean isCurrentPage = (i + 1 == currentPage);
			String styleClass;
			String style;

			if (isCurrentPage) {
				styleClass = (String) component.getAttributes().get("selectedStyleClass");
				style = (String) component.getAttributes().get("selectedStyle");
			} else {
				styleClass = (String) component.getAttributes().get("inactiveStyleClass");
				style = (String) component.getAttributes().get("inactiveStyle");
			}

			if (styleClass == null) {
				styleClass = "";
			}

			if (isCurrentPage) {
				out.startElement(HtmlConstants.SPAN_ELEM, component);
				out.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, " " + styleClass, null);
			} else {
				out.startElement(HtmlConstants.A_ELEMENT, component);
				out.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, " " + styleClass, null);
				out.writeAttribute(HtmlConstants.HREF_ATTR, "javascript:void(0);", null);
			}

			if (null != style) {
				out.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, style, null);
			}

			String page = Integer.toString(i + 1);
			String id = clientId + "_ds_" + page;

			out.writeAttribute(HtmlConstants.ID_ATTRIBUTE, id, null);

			digital.put(id, page);

			out.writeText(page, null);

			if (isCurrentPage) {
				out.endElement(HtmlConstants.SPAN_ELEM);
			} else {
				out.endElement(HtmlConstants.A_ELEMENT);
			}
		}

		return digital;
	}
}
