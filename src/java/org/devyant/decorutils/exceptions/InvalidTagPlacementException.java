/*
 * Copyright 2005 Filipe Tavares
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.devyant.decorutils.exceptions;

import javax.servlet.jsp.JspException;

/**
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 3/Fev/2005 23:18:48
 */
public class InvalidTagPlacementException extends JspException {
    /**
     * Creates a new <code>InvalidTagPlacementException</code> instance.
     * @param tag Tag's name
     */
    public InvalidTagPlacementException(final String tag) {
        super(tag + ": this tag must be nested in an \"X-Tag\".");
    }
}
