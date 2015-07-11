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
package it.vige.rubia.ui.action.validators;

/**
 * This class contains constants with message bundle keys that contain error messages.
 * 
 * @author <a href="ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 *
 */
final public class ValidatorMessages
{
    
    // Messages connected with input length errors.
    public static final String SUBJECT_LENGTH_ERROR = "Empty_subject";
    public static final String MESSAGE_LENGTH_ERROR = "Empty_message";
    
    // Poll oriented messages.
    public static final String EMPTY_POLL_QUESTION_MSG = "Empty_poll_title";
    public static final String EMPTY_POLL_OPTION_MSG = "Empty_poll_option";
    public static final String TOO_FEW_OPTIONS_MSG = "To_few_poll_options";
    public static final String TOO_MANY_OPTIONS_MSG = "To_many_poll_options";
    public static final String POLL_DURATION_MSG = "Wrong_poll_duration";
    
}
