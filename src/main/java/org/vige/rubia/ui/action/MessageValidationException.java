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
package org.vige.rubia.ui.action;

/**
 * 
 * @author <a href="mailto:ryszard.kozmik@jboss.com">Ryszard Kozmik</a>
 *
 */
public class MessageValidationException extends Exception
{
    
    public static final int INVALID = 0;
    public static final int INVALID_POST_SUBJECT = 1;
    public static final int INVALID_POST_TEXT = 2;
    
    private int type;
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MessageValidationException (int type) {
        super();
        this.type = type;
        if ( type != INVALID_POST_SUBJECT  &&
             type != INVALID_POST_TEXT)
        {
            this.type = INVALID;
        }
    }
    
    public int getType() {
        return type;
    }
    
}