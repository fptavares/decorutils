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
package org.devyant.decorutils.xml;

import org.apache.commons.beanutils.LazyDynaBean;
import org.devyant.decorutils.exceptions.InvalidRuleFormatException;
import org.devyant.decorutils.Utils;

import java.util.Collection;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;

/**
 * DynaNode is a <b>cool</b> class.
 *
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 1/Fev/2005 1:51:13
 */
public class DynaNode extends LazyDynaBean {
    /**
     * A Collection of child nodes.
     */
    private Collection children = null;
    /**
     * The parent <code>DynaNode</code>.
     */
    private DynaNode parent = null;
    /**
     * The tag's name.
     */
    private String name = null;
    /**
     * Prefix for class property references.
     */
    private static final String THIS_PREFIX = "this.";

    /**
     * @return Returns the size of the children <code>Collection</code>.
     */
    public final int getChildrenCount() {
        if (children == null) { // prevent NullPointerException
            return 0;           // children==null is equivelent to being empty
        }
        return children.size();
    }

    /**
     * @return The child nodes
     */
    public final Collection getChildren() {
        return children;
    }

    /**
     * @param children The child nodes
     */
    public final void setChildren(Collection children) {
        this.children = children;
    }

    /**
     * @return The parent node
     */
    public final DynaNode getParent() {
        return parent;
    }

    /**
     * @param parent The parent node
     */
    public final void setParent(DynaNode parent) {
        this.parent = parent;
    }

    /**
     * @return The tag's name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name The tag's name
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * Validates node against a set of rules.
     * @param rules Collection of rules
     * @return <code>true</code> if node verifies rules
     * @throws InvalidRuleFormatException Thrown when rules are badly formatted
     * @todo add more operations: ~ (like) and ~* (ilike)
     * @todo add escape '\' support for operators
     * @todo generalize for any operator
     */
    public final boolean verifiesRules(final Collection rules)
        throws InvalidRuleFormatException {
        if (rules == null) { // no rules
            return true;
        }

        boolean verifies = false;

        for (Iterator i = rules.iterator(); i.hasNext();) {
            String rule = String.valueOf(i.next());

            // todo: add more operations: ~ (like) and ~* (ilike)
            // todo: add escape '\' support for operators
            // todo: generalize for any operator
            // lookup which operation is used
            boolean different = true;
            int index = rule.indexOf("!=");
            if (index < 0) {
                different = false;
                index = rule.indexOf("=");
            }

            // invalid rule
            if (index < 0) {
                throw new InvalidRuleFormatException();
            }

            // the attribute's name
            String attrName = rule.substring(0, index);
            // the attribute's value
            String attr = String.valueOf(get(attrName));
            // the attribute's comparison value
            String value = rule.substring(index + (different ? 2 : 1));

            // equal operation value
            verifies = attr.equals(value);

            // different operation value
            if (different) {
                verifies = !verifies;
            }

            if (verifies) {
                break;
            }
        }
        return verifies;
    }

    /**
     * Checks for {@link #THIS_PREFIX}.
     * @see org.apache.commons.beanutils.DynaBean#get(java.lang.String)
     */
    public final Object get(final String name) {
        final Object value;
        if (name.startsWith(THIS_PREFIX)) {
            try {
                value = Utils.getClassProperty(DynaNode.this,
                        name.substring(THIS_PREFIX.length()));
            } catch (IllegalAccessException e) {
                return null;
            } catch (InvocationTargetException e) {
                return null;
            } catch (NoSuchMethodException e) {
                return null;
            }
        } else {
            value = super.get(name);
        }
        return value;
    }
}
