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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Vector;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 2/Fev/2005 15:50:55
 */
public class Utils {

    /**
     * Not allowed.
     */
    protected Utils() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * Split by delim character and ignore surrounding space.
     * @param string The string
     * @return Array of splited atoms
     */
    public static final String[] commaSplitToArray(final String string) {
        return splitToArrayIgnoreSpace(string, ',');
    }

    /**
     * Split by delim character and ignore surrounding space.
     * @param string The string
     * @return Collection of splited atoms
     */
    public static final Collection commaSplitToCollection(final String string) {
        return splitToCollectionIgnoreSpace(string, ',');
    }

    /**
     * Split by delim character and ignore surrounding space.
     * @param string The string
     * @param delim The delimiter character
     * @return Collection of splited atoms
     */
    public static final Collection splitToCollectionIgnoreSpace(
            final String string, final char delim) {
        final String[] attr = splitToArrayIgnoreSpace(string, delim);

        final Vector tokens = new Vector(attr.length);
        for (int i = 0; i < attr.length; i++) {
            tokens.add(i, attr[i]);
        }

        return tokens;
    }

    /**
     * Split by delim character and ignore surrounding space.
     * @param string The string
     * @param delim The delimiter character
     * @return Array of splited atoms
     */
    public static final String[] splitToArrayIgnoreSpace(
            final String string, final char delim) {
        return string.trim().split("\\s*" + delim + "+\\s*");
    }

    /**
     * Capitalize first word of String.
     * @param string The String
     * @return Capitalized String
     */
    public static final String capitalize(final String string) {
        int len;

        if ((string == null) || (len = string.length()) == 0) {
            return string;
        }

        final StringBuffer buf = new StringBuffer(len);
        buf.append(Character.toTitleCase(string.charAt(0)));
        buf.append(string.substring(1));

        return buf.toString();
    }

    /**
     * Decapitalize first word of String.
     * @param string The String
     * @return Decapitalized String
     */
    public static final String decapitalize(final String string) {
        int len;

        if ((string == null) || (len = string.length()) == 0) {
            return string;
        }

        final StringBuffer buf = new StringBuffer(len);
        buf.append(Character.toLowerCase(string.charAt(0)));
        buf.append(string.substring(1));

        return buf.toString();
    }

    /**
     * The getter method for certain property from a certain
     * class. The method can be an is or get method.
     *
     * @param theClass The property's class
     * @param property The property
     * @return The getter method
     * @throws NoSuchMethodException Thrown if method does not exist
     */
    public static final Method getGetterMethod(
            final Class theClass, final String property)
        throws NoSuchMethodException {
        Method decorateMethod;
        final String propertyName = capitalize(property);
        try {
            decorateMethod = theClass
                .getMethod("is" + propertyName, new Class[]{});
        } catch (Exception ex) {
            // no is method, so look for getter
            decorateMethod = theClass
                .getMethod("get" + propertyName, new Class[]{});
        }
        return decorateMethod;
    }
    /**
     * The value for a certain property from the object.
     * @param object The object
     * @param prop The property
     * @return The property's value
     * @throws NoSuchMethodException Thrown if method does not exist
     * @throws IllegalAccessException Thrown if is access protected
     * @throws InvocationTargetException Thrown if target isn't valid
     */
    public static final Object getClassProperty(
            final Object object, final String prop)
        throws NoSuchMethodException,
        IllegalAccessException,
        InvocationTargetException {
        
        // the property
        String property = prop;
        // extra properties
        String extra = null;
        // the properties value
        Object value;
        
        final int index = prop.indexOf(".");
        if (index > 0) {
            property = prop.substring(0, index);
            extra = prop.substring(index + 1);
        }
        
        value = getGetterMethod(object.getClass(), property)
            .invoke(object, new Object[]{});
        
        if ((value != null) && (extra != null)) {
            PropertyUtils.getProperty(value, extra);
        }
        
        return value;
    }
}
