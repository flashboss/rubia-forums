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
package it.vige.rubia.properties;

import static it.vige.rubia.Constants.THEMENAME;
import static it.vige.rubia.ui.JSFUtil.getContextPath;
import static java.lang.Thread.currentThread;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 878 $
 */
public class TCCLXProperties extends XProperties {
	private Map<Object, Object> props = new HashMap<Object, Object>();
	private Map<Object, Object> urls = new HashMap<Object, Object>();

	/**
	 * Creates a new {@link TCCLXProperties} object.
	 * 
	 * @param base
	 *            DOCUMENT_ME
	 * @param name
	 *            DOCUMENT_ME
	 * @throws IOException
	 *             DOCUMENT_ME
	 */
	public TCCLXProperties(String base, String name) throws IOException {
		this(currentThread().getContextClassLoader(), base, name);
	}

	/**
	 * Creates a new {@link TCCLXProperties} object.
	 * 
	 * @param loader
	 *            DOCUMENT_ME
	 * @param base
	 *            DOCUMENT_ME
	 * @param name
	 *            DOCUMENT_ME
	 * @throws IOException
	 *             DOCUMENT_ME
	 */
	public TCCLXProperties(ClassLoader loader, String base, String name) throws IOException {
		InputStream in = null;
		try {
			// load the data
			in = loader.getResourceAsStream(base + "/" + name);

			// feed the properties
			Properties temp = new Properties();
			temp.load(in);
			props.putAll(temp);
			for (Iterator<Map.Entry<Object, Object>> i = props.entrySet().iterator(); i.hasNext();) {
				Map.Entry<Object, Object> entry = i.next();

				urls.put(entry.getKey(), getContextPath() + "/" + THEMENAME + "/" + entry.getValue());
			}
		} finally {
			in.close();
		}
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param name
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 */
	public String getProperty(String name) {
		Object value = props.get(name);
		return (value != null) ? (String) value : "";
	}

	/**
	 * DOCUMENT_ME
	 * 
	 * @param name
	 *            DOCUMENT_ME
	 * @return DOCUMENT_ME
	 */
	public String getResourceURL(String name) {
		Object value = urls.get(name);
		return (value != null) ? (String) value : "";
	}
}