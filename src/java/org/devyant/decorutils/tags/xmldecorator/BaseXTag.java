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
package org.devyant.decorutils.tags.xmldecorator;

import org.devyant.decorutils.Utils;
import org.devyant.decorutils.xml.DynaNode;

import java.util.Collection;

/**
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 2/Fev/2005 3:32:13
 */
public class BaseXTag extends RuledTag {
    /**
     * The current <code>DynaNode</code>.
     */
    private DynaNode current;
    /**
     * The attributes you wish to manipulate. They will be stored in
     * the scope with the same name.
     */
    private Collection attributesCollection = null;

    /**
     * @return The current iteration node
     */
    public DynaNode getCurrent() {
        return current;
    }

    /**
     * @param current The current iteration node
     */
    public final void setCurrent(DynaNode current) {
        this.current = current;
    }
    
    /**
     * @return Returns the attributes.
     */
    public Collection getAttributesCollection() {
        return attributesCollection;
    }
    /**
     * @param attributes The attributes <code>Vector</code> to set.
     */
    public void setAttributesCollection(Collection attributes) {
        this.attributesCollection = attributes;
    }
    
    /**
     * The attributes you wish to manipulate. They will be stored in
     * the scope with the same name.
     *
     * @param attributes The attributes separated by commas
     */
    public void setAttributes(String attributes) {
        this.attributesCollection = Utils.commaSplitToCollection(attributes);
    }

    /**
     * Common tasks used by every
     * child class in the <code>eval()</code> method.
     *
     * @param node The current node
     */
    protected final void doEvalCommon(DynaNode node) {
        setGoRuleElse(true); // start rule else has true at the beggining of cycle
        setCurrent(node);
    }
}
