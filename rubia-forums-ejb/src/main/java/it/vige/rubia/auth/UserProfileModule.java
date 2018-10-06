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

import java.util.Map;

import it.vige.rubia.dto.UserPropertyBean;

public interface UserProfileModule {

	Object getProperty(UserPropertyBean userProperty) throws IllegalArgumentException;

	Object getPropertyFromId(String arg0, String arg1) throws IllegalArgumentException;

	void setProperty(UserPropertyBean userProperty) throws IllegalArgumentException;

	Map<String, String> getProperties(User user) throws IllegalArgumentException;

	ProfileInfo getProfileInfo();
}
