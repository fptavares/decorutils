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
package org.devyant.decorutils.tags.decorator;

import org.devyant.decorutils.Wrapper;
import org.devyant.decorutils.decorators.DateDecorator;

/**
 * TestWrapper is a <b>cool</b> class.
 * 
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 22/Mar/2005 2:22:37
 */
public class TestWrapper extends Wrapper {
    /**
     * The format <code>String</code>.
     */
    private String format;
    
    /**
     * @return A String representing the date
     */
    public final String getString() {
        final DateDecorator decorator = new DateDecorator();
        decorator.setFormat(format);
        return (String) decorator.decorate(getObject());
    }

    /**
     * @return Returns the format.
     */
    public final String getFormat() {
        return format;
    }
    /**
     * @param format The format <code>String</code> to set.
     */
    public final void setFormat(final String format) {
        this.format = format;
    }
}
