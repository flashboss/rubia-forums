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
package it.vige.rubia.format.render.bbcodehtml;

import java.util.ResourceBundle;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5448 $
 */
public class ToHTMLRenderer {

	/*
	 * Different parsers.
	 */

	/*
	 * Templates.
	 */

	/** Setup the renderer with the given template repository. */
	public ToHTMLRenderer(ToHTMLConfig config, ResourceBundle bundle) {
	}

	public ToHTMLRenderer(ResourceBundle bundle) {
		this(new ToHTMLConfig(), bundle);
	}

	public ToHTMLConfig getConfig() {
		return null;
	}

	public void setConfig(ToHTMLConfig config) {
	}

	public void render(char[] chars, int offset, int length) {
	}
}
