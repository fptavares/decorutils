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
 * You do NOT have to implement this method to be able
 * to use the Decorator Tag Library. This method
 * only exists has a guide-line for creating decorators.
 * <p>
 * All your class must have is simply a method <code>decorate</code>
 * that receives an <code>Object</code> and <i>decorates</i> it.
 * </p>
 *
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 14/12/2004 1:09:49
 */
public interface Decorator {
    /**
     * @param object The object to decorate
     * @return The decorated object
     */
    Object decorate(Object object);
}
