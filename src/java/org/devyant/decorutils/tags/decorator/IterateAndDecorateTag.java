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

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.IterationTag;

import org.devyant.decorutils.exceptions.DecoratorException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

/**
 * Works the same way as the {@link DecorateTag} but expects a
 * <code>Collection</code> and interates through it's items and stores
 * the resulting object of each decoration for you to manipulate within
 * the body of the tag.
 *
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 4/Fev/2005 2:34:03
 * @see org.devyant.decorutils.tags.decorator.DecorateTag
 */
public class IterateAndDecorateTag
    extends DecorateAndStoreTag implements IterationTag {
    /**
     * The items <code>Iterator</code>.
     */
    private Iterator items;
    /**
     * The initRow <code>Method</code>.
     */
    private Method initRow;
    /**
     * The decorator <code>Object</code>.
     */
    private Object decorator;
    /**
     * The decorateMethod <code>Method</code>.
     */
    private Method decorateMethod;

    /**
     * @see org.devyant.decorutils.tags.decorator.DecorateTag#returnDecoratedObject(java.lang.Object)
     */
    protected void returnDecoratedObject(final Object r) {
    }

    /**
     * @see org.devyant.decorutils.tags.decorator.DecorateTag#invokeDecorateMethodForObject(
     * java.lang.Object, java.lang.reflect.Method, java.lang.Object,
     * java.lang.reflect.Method)
     */
    protected Object invokeDecorateMethodForObject(
            final Object object, final Method initRow,
            final Object decorator, final Method decorateMethod) {
        items = ((Collection) object).iterator();

        this.initRow = initRow;
        this.decorator = decorator;
        this.decorateMethod = decorateMethod;

        return null;
    }

    /**
     * @see org.devyant.decorutils.tags.decorator.DecorateTag#getReturnConst()
     */
    protected int getReturnConst() {
        try {
            return eval();
        } catch (JspTagException e) {
            return SKIP_BODY;
        }
    }

    /**
     * @return Return constant
     * @throws JspTagException Could not apply decorator to object
     */
    private int eval() throws JspTagException {
        if (items.hasNext()) {
            final Object object = items.next();

            Object result = null;
            try {
                result = super.invokeDecorateMethodForObject(
                        object, initRow, decorator,  decorateMethod);
            } catch (DecoratorException e) {
                throw new JspTagException("IterateAndDecorateTag: "
                        + e.getMessage());
            }

            pageContext.setAttribute(getId(), result);

            return EVAL_BODY_BUFFERED;
        } else {
            return SKIP_BODY;
        }
    }

    /**
     * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
     */
    public int doAfterBody() throws JspTagException {
        final BodyContent body = getBodyContent();
        try {
            body.writeOut(getPreviousOut());
        } catch (IOException e) {
            throw new JspTagException("XDecorateTag: " + e.getMessage());
        }

        // clear up so the next time the body content is empty
        body.clearBody();

        return eval();
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() {
        pageContext.removeAttribute(getId());
        return EVAL_PAGE;
    }
}
