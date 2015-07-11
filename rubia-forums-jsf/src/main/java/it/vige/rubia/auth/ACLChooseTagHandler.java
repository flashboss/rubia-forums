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
package it.vige.rubia.auth;

//core java
import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.CompositeFaceletHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletHandler;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

/**
 * 
 * @author Sohil Shah - sohil.shah@jboss.com - Mar 29, 2006
 * 
 */
public class ACLChooseTagHandler extends TagHandler {

	/**
     * 
     */
	private ACLWhenTagHandler when;
	private ACLOtherwiseTagHandler otherwise;

	/**
	 * @param config
	 */
	public ACLChooseTagHandler(TagConfig config) {
		super(config);

		// setup when tag handler
		FaceletHandler itr = nextHandler;
		if (itr instanceof CompositeFaceletHandler) {
			FaceletHandler[] handlers = ((CompositeFaceletHandler) itr)
					.getHandlers();
			if (handlers != null && handlers.length > 0
					&& handlers[0] instanceof ACLWhenTagHandler) {
				when = (ACLWhenTagHandler) handlers[0];
			} else {
				throw new TagException(tag,
						"isAllowedChoose Tag must have a isAllowedWhen Tag");
			}

			// setup otherwise tag handler
			if (handlers.length > 1) {
				FaceletHandler itr2 = handlers[1];
				if (itr2 instanceof ACLOtherwiseTagHandler) {
					otherwise = (ACLOtherwiseTagHandler) itr2;
				}
			}
		} else {
			throw new TagException(tag,
					"isAllowedChoose Tag must have a CompositeFaceletHandler Tag");
		}
	}

	/**
	 * Threadsafe Method for controlling evaluation of its child tags,
	 * represented by "nextHandler"
	 */
	public void apply(FaceletContext ctx, UIComponent parent)
			throws IOException, FacesException, ELException {
		if (when.isAllowed(ctx)) {
			when.apply(ctx, parent);
			return;
		} else {
			if (otherwise != null) {
				otherwise.apply(ctx, parent);
			}
		}
	}
}
