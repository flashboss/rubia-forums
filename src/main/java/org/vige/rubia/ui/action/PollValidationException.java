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
public class PollValidationException extends Exception
{

    public static final int INVALID = 0;
    public static final int INVALID_POLL_OPTION = 1;
    public static final int TOO_FEW_POLL_OPTION = 2;
    public static final int TOO_MANY_POLL_OPTION = 3;
    public static final int INVALID_POLL_TITLE = 4;
    
    private int type;
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PollValidationException (int type) {
        super();
        this.type = type;
        if ( type != INVALID_POLL_OPTION  &&
             type != INVALID_POLL_TITLE   &&
             type != TOO_FEW_POLL_OPTION &&
             type != TOO_MANY_POLL_OPTION  )
        {
            this.type = INVALID;
        }
    }
    
    public int getType() {
        return type;
    }
    
}
