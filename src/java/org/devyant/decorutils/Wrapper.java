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
package org.devyant.decorutils;

/**
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 28/Fev/2005 5:50:17
 */
public abstract class Wrapper {
    /**
     * The object <code>Object</code>.
     */
    private Object object;

    /**
     * @return The <code>Object</code> to decorate
     */
    protected final Object getObject() {
        return object;
    }

    /**
     * Initialize the wrapper.
     * @param object The <code>Object</code> to decorate
     */
    public final void init(final Object object) {
        this.object = object;
    }
}
