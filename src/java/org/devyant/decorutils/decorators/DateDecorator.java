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
package org.devyant.decorutils.decorators;

import org.devyant.decorutils.Decorator;

import javax.servlet.jsp.PageContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Decorator for transforming <code>Date</code> instances
 * for example and utility purpose.
 *
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 16/Fev/2005 4:34:28
 */
public class DateDecorator implements Decorator {
    /**
     * The DEFAULT_LOCALE <code>Locale</code>.
     */
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    /**
     * The <code>Locale</code> to be used.
     */
    private transient Locale locale = DEFAULT_LOCALE;
    /**
     * <code>DateFormat</code> constant value.
     */
    private transient int dateFormat = DateFormat.FULL;

    /**
     * Custom format for the date.
     */
    private transient String customFormat = null;

    /**
     * The decorate method.
     * @param object Object to decorate
     * @return The decorated object
     */
    public final Object decorate(final Object object) {
        return getDateString((Date) object);
    }
    /**
     * <p>Format <code>Date</code> and return resulting <code>String</code>.</p>
     * @param date The date instance
     * @return Formatted <code>String</code>
     */
    private String getDateString(final Date date) {
        return getDateFormatter().format(date);
    }

    /**
     * <p>Return the corresponding <code>DateFormat</code> instance.</p>
     * @return The <code>DateFormat</code> instance
     */
    private DateFormat getDateFormatter() {
        if (customFormat == null) {
            return DateFormat.getDateInstance(dateFormat, locale);
        } else {
            return new SimpleDateFormat(customFormat, locale);
        }
    }

    /**
     * <p>Setter method for the <i>pageContext</i> attribute.</p>
     * @param pageContext The page context
     */
    public final void setPageContext(final PageContext pageContext) {
        this.locale = pageContext.getRequest().getLocale();
    }

    /**
     * <p>Setter method for the <i>format</i> attribute.</p>
     * @param format The date format
     */
    public final void setFormat(final String format) {
        if (format.equalsIgnoreCase("full")) {
            dateFormat = DateFormat.FULL;
        } else if (format.equalsIgnoreCase("long")) {
            dateFormat = DateFormat.LONG;
        } else if (format.equalsIgnoreCase("medium")) {
            dateFormat = DateFormat.MEDIUM;
        } else if (format.equalsIgnoreCase("short")) {
            dateFormat = DateFormat.SHORT;
        } else if (format.equalsIgnoreCase("fixed")) {
            customFormat = "dd MMMM yyyy HH:mm";
        } else {
            customFormat = format;
        }
    }
}
