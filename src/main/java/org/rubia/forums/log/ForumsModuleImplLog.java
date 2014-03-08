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
package org.rubia.forums.log;

import static org.jboss.solder.logging.Logger.Level.ERROR;

import org.jboss.solder.logging.Log;
import org.jboss.solder.logging.MessageLogger;
import org.jboss.solder.messages.Message;

@MessageLogger
public interface ForumsModuleImplLog {

	@Log(level = ERROR)
	@Message("Aviability for warning with code %s.")
	void error(String error);

	@Log(level = ERROR)
	@Message("Aviability for warning with code %s: %s.")
	void error(String error, Exception exception);

}
