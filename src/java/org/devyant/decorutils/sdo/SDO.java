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
package org.devyant.decorutils.sdo;

import java.text.Format;
import java.util.Comparator;

/**
 * Sortable Decorated Object.
 * 
 * @author Filipe Tavares
 * @version @version $Revision$ ($Author$)
 * @since Dec 8, 2004 7:29:42 PM
 *
 * @see java.lang.Comparable
 * @see java.util.Comparator
 */
public class SDO implements Displayable {
    /**
     * The <code>String</code> to output.
     */
    private transient String displayString;
    /**
     * The <code>Object</code> to be used in comparison.
     */
    private final transient Object object;
    /**
     * The comparator.
     */
    private transient Comparator comparator = null;

    /**
     * Constructs a new, empty Sortable Decorated Object.
     */
    public SDO() {
        object = "";
        displayString = "";
    }

    /**
     * Constructs a new Sortable Decorated Object with an empty
     * display <code>String</code>.
     *
     * @param object <code>Comparable</code> object for sorting purposes
     */
    public SDO(final Comparable object) {
        this.object = object;
        displayString = "";
    }

    /**
     * Constructs a new Sortable Decorated Object and creates display
     * <code>String</code> using the specified formatter on the object.
     *
     * @param object    <code>Comparable</code> object for sorting purposes
     * @param formatter output formmater
     */
    public SDO(final Comparable object, final Format formatter) {
        this.object = object;
        displayString = formatter.format(object);
    }

    /**
     * Constructs a new Sortable Decorated Object.
     *
     * @param object        <code>Comparable</code> object for sorting purposes
     * @param displayString <code>String</code> to return on
     * the <code>toString()</code> method
     */
    public SDO(final Comparable object, final String displayString) {
        this.object = object;
        this.displayString = displayString;
    }

    /**
     * Constructs a new Sortable Decorated Object and creates display
     * <code>String</code> using the specified formatter on the object.
     *
     * @param comparator <code>Comparator</code> for sorting purposes
     * @param object     decorated object
     * @param formatter  output formmater
     */
    public SDO(final Comparator comparator,
            final Object object, final Format formatter) {
        this.object = object;
        this.comparator = comparator;
        this.displayString = formatter.format(object);
    }

    /**
     * Constructs a new Sortable Decorated Object.
     *
     * @param comparator    <code>Comparator</code> for sorting purposes
     * @param object        decorated object
     * @param displayString <code>String</code> to return
     * on <code>toString()</code> method
     */
    public SDO(final Comparator comparator,
            final Object object, final String displayString) {
        this.object = object;
        this.comparator = comparator;
        this.displayString = displayString;
    }

    /**
     * This method overrides <code>Object</code> <code>toString</code>
     * method returning the specified output <code>String</code>.
     * 
     * @return decorated output
     */
    public final String toString() {
        return displayString;
    }

    /**
     * @see Comparable#compareTo(java.lang.Object)
     */
    public final int compareTo(final Object o) {
        final SDO sdo = (SDO) o;
        if (object instanceof Comparable) {
            return ((Comparable) object).compareTo(sdo.getObject());
        } else {
            return comparator.compare(object, o);
        }
    }

    /**
     * Set the output <code>String</code>.
     *
     * @param s output <code>String</code>
     */
    public final void setDisplayString(final String s) {
        displayString = s;
    }

    /**
     * Returns the decorated object.
     *
     * @return the object
     */
    public final Object getObject() {
        return object;
    }
}
