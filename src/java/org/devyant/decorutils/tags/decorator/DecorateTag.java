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

import org.apache.commons.beanutils.PropertyUtils;
import org.devyant.decorutils.Utils;
import org.devyant.decorutils.Wrapper;
import org.devyant.decorutils.exceptions.DecoratorException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

/**
 * This tag is usefull for decorating objects, transforming the data
 * containers in presentable text data. This is done by aplying a decorator
 * to the object we wish to decorate.
 * <p>
 * Decorators may be in two different forms: a class with a public method
 * <code>decorate</code> receiving an <code>Object</code> to decorate and
 * returning another <code>Object</code> (Decorator) or a <code>Wrapper</code>
 * whose design was specified by the Decorator Design Pattern. A wrapper should
 * implement the accessor methods that you wish to override from the decoree
 * object.
 * </p>
 * <p>Examples:</p>
 * <p><pre><code>
 *     public class MyDecorator {
 *   
 *         public Object decorate(Object object) {
 *             // decorate
 *             return decoratedObject;
 *         }
 *       
 *     }
 * </code></pre></p>
 * <p><pre><code>
 * public class MyWrapper extends Wrapper {
 *   
 *     public String getName() {
 *         MyObject myObject = (MyObject) getObject();
 *         return myObject.getParent().getName()
 *             + " -> "
 *             + myObject.getName();
 *     }
 *      
 *     public String getDate() {
 *         // format date
 *         return dateString;
 *     }
 *      
 * }
 * </code></pre></p>
 * 
 * @author  Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since Dec 7, 2004 6:34:12 PM
 */
public class DecorateTag extends BodyTagSupport {
    /**
     * Decorator class to be used to decorate.
     */
    private String decorator = null;
    /**
     * The name of the bean owning the property to be retrieved.
     */
    private String name = null;
    /**
     * The name of the property to be retrieved.
     */
    private String property = null;
    /**
     * The scope within which to search for the specified bean.
     * Default is page scope.
     */
    private String scope = null;
    /**
     * A set of extra attributes that you may wish to add to your decorator
     * through setter methods. The set of attributes should be seperated by
     * commas (",").
     * <p>
     * The attributes may be static or dynamic. For static attributes you must
     * indicate the name of the property and it's value in the following manner:
     * <code>property=value</code>. Dynamic attributes only require the name of
     * the bean to retrieve, which will also be the name of the property to set.
     * </p>
     * <p>
     * In the end, the attributes set should look something like this:
     * <pre>
     *       thing, staticStuff=bla bla bla bla bla, moreStuff, evenMoreStuff
     * </pre>
     * </p>
     */
    private String attributes = null;
    /**
     * If set to true, the decorator will be populated with the
     * <code>PageContext</code> instance. Setter methods for pageContext must
     * exist in the decorator class (<code>setPageContext</code>).
     * <p>
     * Default value is <code>false</code>.
     * </p>
     */
    private boolean setPageContext = false;
    /**
     * When set to true, the decorator expects a <code>Collection</code> of
     * values to decorate and iterates through the <code>Collection</code>
     * decorating each object seperatly.
     * <p>
     * Default value is <code>false</code>.
     * </p>
     */
    private boolean iterate = false;
    /**
     * The isWrapper <code>boolean</code>.
     */
    private transient boolean isWrapper = false;


    /* tag section */

    /**
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
        try {
            Object object = null;

            /* decorator */
            final Class decoratorClass = Class.forName(decorator);
            final Object decorator = decoratorClass.newInstance();
            Method decorateMethod = null;
            Method initRow = null;
            try {

                // simple decorator: Object decorator(Object);
                decorateMethod = decoratorClass.getMethod(
                        "decorate",
                        new Class[]{Object.class});

            } catch (NoSuchMethodException e) {

                // wrapper case
                isWrapper = true;
                /*decorateMethod = Utils.getGetterMethod(
                        decoratorClass,
                        property);*/

                // the object to decorate
                object = getObject(name, null);

                if (!(decorator instanceof Wrapper)) {
                    // try TableDecorator case
                    final Method initMethod = decoratorClass.getMethod(
                            "init",
                            new Class[]{PageContext.class, Object.class});
                    initMethod.invoke(
                            decorator,
                            new Object[]{pageContext, (iterate) ? object : null});
    
                    initRow = decoratorClass.getMethod(
                            "initRow",
                            new Class[]{Object.class, int.class, int.class});
                    
                }
            }
            setAttributes(decorator);
            setPageContext(decorator);


            // the object to decorate
            if (!isWrapper) {
                object = getObject(name, property); // not a wrapper
            }

            // invoke decorate method for obejct o
            final Object result = invokeDecorateMethodForObject(
                    object, initRow, decorator, decorateMethod);

            // print
            returnDecoratedObject(result);


        } catch (Exception e) {
            // store exception stack trace
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(stream));

            throw new JspTagException("DecorateTag: " + stream.toString());
        }
        return getReturnConst();
    }

    /**
     * @param object Object to decorate
     * @param initRow <code>initRow</code> method
     * @param decorator Decorator instance
     * @param decorateMethod <code>decorate</code> method
     * @return Decorated <code>Object</code>
     * @throws DecoratorException Could not decorate
     */
    protected Object invokeDecorateMethodForObject(
            final Object object, final Method initRow,
            final Object decorator, final Method decorateMethod)
        throws DecoratorException {
        String result = "";
        if (iterate) {
            final Iterator i = ((Collection) object).iterator();
            for (int l = 0; i.hasNext(); l++) {
                result += invokeMethod(
                        initRow, decorator, i.next(), l, decorateMethod);
            }
        } else {
            return invokeMethod(initRow, decorator, object, 0, decorateMethod);
        }
        return result;
    }

    /**
     * @return Constant to return on {@link #doStartTag()}
     */
    protected int getReturnConst() {
        return SKIP_BODY;
    }

    /**
     * @param r The object
     * @throws Exception Could not return the object
     */
    protected void returnDecoratedObject(final Object r) throws Exception {
        pageContext.getOut().print(r);
    }

    /**
     * @param initRow <code>initRow</code> method
     * @param decorator Decorator instance
     * @param object Object to decorate
     * @param index index
     * @param decorateMethod <code>decorate</code> method
     * @return Decorated <code>Object</code>
     * @throws DecoratorException Could not decorate
     */
    private Object invokeMethod(
            final Method initRow, final Object decorator, final Object object,
            final int index, final Method decorateMethod)
        throws DecoratorException {
        final Object result;
        try {
            if (isWrapper) {
                if (initRow == null) {
                    // initialize wrapper with the object
                    ((Wrapper) decorator).init(object);
                } else { // TableDecorator
                    initRow.invoke(
                            decorator,
                            new Object[] {
                                object, new Integer(index),
                                new Integer(index)
                            });
                }
                // call getter
                //result = decorateMethod.invoke(decorator, new Object[]{});
                result = PropertyUtils.getProperty(decorator, property);
            } else { // decorator
                // call decorate()
                result = decorateMethod.invoke(decorator, new Object[]{object});
            }
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new DecoratorException(e.getTargetException());
        } catch (IllegalArgumentException e) {
            throw new DecoratorException(e);
        } catch (IllegalAccessException e) {
            throw new DecoratorException(e);
        } catch (NoSuchMethodException e) {
            throw new DecoratorException(e);
        }
        return result;
    }

    /**
     * @param decorator Decorator instance
     * @throws NoSuchMethodException Thrown if method does not exist
     * @throws IllegalAccessException Thrown if is access protected
     * @throws InvocationTargetException Thrown if target isn't valid
     */
    private void setPageContext(final Object decorator)
        throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        // pageContext
        if (setPageContext) {
            PropertyUtils.setProperty(decorator, "pageContext", pageContext);
        }
    }

    /**
     * @param decorator Decorator instance
     * @throws JspTagException The bean could not be retrieved
     * @throws NoSuchMethodException Thrown if method does not exist
     * @throws IllegalAccessException Thrown if is access protected
     * @throws InvocationTargetException Thrown if target isn't valid
     */
    private void setAttributes(final Object decorator)
        throws JspTagException, IllegalAccessException,
        InvocationTargetException, NoSuchMethodException {
        // attributes
        if (attributes != null) {
            final String[] attr = Utils.commaSplitToArray(attributes);
            for (int i = 0; i < attr.length; i++) {

                final int index = attr[i].indexOf("=");

                if ((index < 0) || ((index + 1) >= attr[i].length())) {
                    PropertyUtils.setProperty(decorator, attr[i],
                            getObject(attr[i], null));
                } else {
                    final String property = attr[i].substring(0, index);
                    final String value = attr[i].substring(index + 1);
                    PropertyUtils.setProperty(decorator, property, value);
                }
            }
        }
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() {
        return EVAL_PAGE;
    }

    /* utils stuff */

    /**
     * @param name The name of the bean owning the property to be retrieved
     * @param property The name of the property to be retrieved
     * @return The bean
     * @throws JspTagException The bean could not be retrieved
     * @throws NoSuchMethodException Thrown if method does not exist
     * @throws IllegalAccessException Thrown if is access protected
     * @throws InvocationTargetException Thrown if target isn't valid
     */
    private Object getObject(final String name, final String property)
        throws JspTagException, IllegalAccessException,
        InvocationTargetException, NoSuchMethodException {
        Object object = null;

        final int index = name.indexOf('.');
        if ((index > 0) && ((index + 1) < name.length())) {
            object = getBean(name.substring(0, index));
            object = getProperty(object, name.substring(index + 1));
        } else {
            object = getBean(name);
        }

        if (object == null) {
            throw new JspTagException("The bean "
                    + name + " could not be found.");
        }

        if (property != null) {
            object = getProperty(object, property);

            if (object == null) {
                throw new JspTagException("The property " + property
                        + " could not be found for bean " + name + ".");
            }
        }

        return object;
    }

    /**
     * @param object The bean owning the property to be retrieved
     * @param property The name of the property to be retrieved
     * @return The property's value
     * @throws NoSuchMethodException Thrown if method does not exist
     * @throws IllegalAccessException Thrown if is access protected
     * @throws InvocationTargetException Thrown if target isn't valid
     */
    private Object getProperty(final Object object, final String property)
        throws IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return PropertyUtils.getProperty(object, property);
    }

    /**
     * Get bean from scope.
     * @param name The name of the bean to retrieve
     * @return The bean
     * @throws JspTagException Throw when the specified scope isn't valid
     */
    private Object getBean(final String name) throws JspTagException {
        Object object;
        if (scope == null) {
            object = pageContext.findAttribute(name);
        } else {
            object = pageContext.getAttribute(name, getScopeConst(scope));
        }
        return object;
    }

    /**
     * @param scope The scope
     * @return Scope identifier
     * @throws JspTagException Throw when the specified scope isn't valid
     */
    protected final int getScopeConst(final String scope)
        throws JspTagException {
        if (scope == null) {
            return PageContext.PAGE_SCOPE;
        }

        int theScope = 0;
        if (scope.equalsIgnoreCase("page")) {
            theScope = PageContext.PAGE_SCOPE;
        } else if (scope.equalsIgnoreCase("request")) {
            theScope = PageContext.REQUEST_SCOPE;
        } else if (scope.equalsIgnoreCase("session")) {
            theScope = PageContext.SESSION_SCOPE;
        } else if (scope.equalsIgnoreCase("application")) {
            theScope = PageContext.APPLICATION_SCOPE;
        } else {
            throw new JspTagException("Invalid scope: " + this.scope + ".");
        }
        return theScope;
    }


    /* getter and setter methods */

    
    /**
     * @return Returns the attributes.
     */
    public final String getAttributes() {
        return attributes;
    }
    /**
     * @param attributes The attributes <code>String</code> to set.
     */
    public final void setAttributes(final String attributes) {
        this.attributes = attributes;
    }
    /**
     * @return Returns the decorator.
     */
    public final String getDecorator() {
        return decorator;
    }
    /**
     * @param decorator The decorator <code>String</code> to set.
     */
    public final void setDecorator(final String decorator) {
        this.decorator = decorator;
    }
    /**
     * @return Returns the iterate.
     */
    public final boolean isIterate() {
        return iterate;
    }
    /**
     * @param iterate The iterate <code>boolean</code> to set.
     */
    public final void setIterate(final boolean iterate) {
        this.iterate = iterate;
    }
    /**
     * @return Returns the name.
     */
    public final String getName() {
        return name;
    }
    /**
     * @param name The name <code>String</code> to set.
     */
    public final void setName(final String name) {
        this.name = name;
    }
    /**
     * @return Returns the property.
     */
    public final String getProperty() {
        return property;
    }
    /**
     * @param property The property <code>String</code> to set.
     */
    public final void setProperty(final String property) {
        this.property = property;
    }
    /**
     * @return Returns the scope.
     */
    public final String getScope() {
        return scope;
    }
    /**
     * @param scope The scope <code>String</code> to set.
     */
    public final void setScope(final String scope) {
        this.scope = scope;
    }
    /**
     * @return Returns the setPageContext.
     */
    public final boolean isSetPageContext() {
        return setPageContext;
    }
    /**
     * @param setPageContext The setPageContext <code>boolean</code> to set.
     */
    public final void setSetPageContext(final boolean setPageContext) {
        this.setPageContext = setPageContext;
    }
}
