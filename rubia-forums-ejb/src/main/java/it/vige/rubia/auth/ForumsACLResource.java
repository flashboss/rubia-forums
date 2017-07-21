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

import static org.jboss.logging.Logger.getLogger;
import static org.jboss.security.authorization.ResourceType.ACL;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.jboss.logging.Logger;
import org.jboss.security.authorization.Resource;
import org.jboss.security.authorization.ResourceType;

@Entity
@Table(name = "ACL_RESOURCE")
public class ForumsACLResource implements Resource {

	private static Logger log = getLogger(ForumsACLResource.class);
	
	@Transient
	private Map<String, Object> map = new HashMap<String, Object>();
	@Id
	private String id;
	@Column(name = "criteria")
	private String criteria;

	public ForumsACLResource() {
	}

	public ForumsACLResource(String id) {
		this.id = id;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	@Override
	public ResourceType getLayer() {
		return ACL;
	}

	@Override
	public Map<String, Object> getMap() {
		return map;
	}

	@Override
	public void add(String key, Object value) {
		map.put(key, value);
	}

	public String getId() {
		return id;
	}

	public boolean evaluate() {
		boolean isCriteriaMet = true;
		if (criteria != null) {
			try {
				JexlEngine jexl = new JexlEngine();
				JexlContext context2 = new MapContext();
				if (criteria != null) {
					Expression expression = jexl.createExpression(criteria);
					context2.set("param", map.get("runtimeInfo"));
					context2.set("identity", map.get("identity"));
					Object value = expression.evaluate(context2);
					isCriteriaMet = ((Boolean) value).booleanValue();
				}
			} catch (Exception e) {
				log.error(e);
				isCriteriaMet = false;
			}
		}

		return isCriteriaMet;
	}
}
