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



/**
 * Does the same as the {@link DecorateTag}, except it stores the decorated
 * object instead of printing it.
 *
 * @author  Filipe Tavares
 * @version $Revision$ ($Author$)
 * @see org.devyant.decorutils.tags.decorator.DecorateTag
 */
public class DecorateAndStoreTag extends DecorateTag {
    /**
     * Stores the result of the decoration in the specified scope instead of
     * printing it. If the scope is not specified, the default scope is the
     * page scope.
     */
    protected String id = null;
    /**
     * The scope in which the result will be stored.
     */
    private String toScope = null;

    /**
     * @see DecorateTag#returnDecoratedObject(java.lang.Object)
     * @throws Exception {@link DecorateTag#returnDecoratedObject(Object)}
     */
    protected void returnDecoratedObject(final Object decoratedObject)
        throws Exception {
        pageContext.setAttribute(id, decoratedObject, getScopeConst(toScope));
    }
    
    /**
     * @return Returns the id.
     */
    public final String getId() {
        return id;
    }
    /**
     * @param id The id <code>String</code> to set.
     */
    public final void setId(final String id) {
        this.id = id;
    }
    /**
     * @return Returns the toScope.
     */
    public final String getToScope() {
        return toScope;
    }
    /**
     * @param toScope The toScope <code>String</code> to set.
     */
    public final void setToScope(final String toScope) {
        this.toScope = toScope;
    }
}
