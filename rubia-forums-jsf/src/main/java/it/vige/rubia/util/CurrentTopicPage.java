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
package it.vige.rubia.util;

import java.io.Serializable;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.primefaces.event.data.PageEvent;

import it.vige.rubia.ui.action.PreferenceController;
import it.vige.rubia.ui.view.ViewTopic;

@Named
@RequestScoped
public class CurrentTopicPage implements Serializable {

	@Inject
	private PreferenceController preferenceController;

	@Inject
	private ViewTopic topic;

	private static final long serialVersionUID = -8034301938112065435L;
	private int page = 0;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void onPage(AjaxBehaviorEvent event) {
		PageEvent pageEvent = (PageEvent) event;
		page = pageEvent.getPage() + 1;
	}

	public int getRow() {
		if (topic.getLastPageNumber() <= 1)
			return 0;
		else
			return (page * preferenceController.getPostsPerTopic());
	}
}
