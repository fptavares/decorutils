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


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * DecorateAndStoreTagTest is a <b>cool</b> class.
 * 
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 18/Mar/2005 2:39:13
 */
public class DecorateAndStoreTagTest extends AbstractDecoratorTaglibTestCase {
    /**
     * The REQUEST_TEST <code>String</code>.
     */
    private static final String REQUEST_TEST = "request_test";
    
    /**
     * The <code>DecorateTag</code> test for decorators.
     * @throws JspException {@link DecorateTag#doStartTag()}
     */
    public final void testDecorators() throws JspException {
        final DecorateAndStoreTag tag = (DecorateAndStoreTag) getTag();
        tag.setToScope("request");
        tag.setId(REQUEST_TEST);
        
        assertEquals("doStartTag() did not return SKIP_BODY.",
                Tag.SKIP_BODY, tag.doStartTag());
        assertEquals("doEndTag() did not return EVAL_PAGE.",
                Tag.EVAL_PAGE, tag.doEndTag());
        
        verifyOutput((String) request.getAttribute(REQUEST_TEST));
    }
    
    /**
     * The <code>DecorateTag</code> test for wrappers.
     * @throws JspException {@link DecorateTag#doStartTag()}
     */
    public final void testWrappers() throws JspException {
        final DecorateAndStoreTag tag = (DecorateAndStoreTag) getTag();
        tag.setToScope("request");
        tag.setId(REQUEST_TEST);
        
        tag.setDecorator(WRAPPER_CLASS);
        tag.setName(BEAN_KEY + "." + PROPERTY_KEY);
        tag.setProperty(WRAPPER_PROPERTY);
        
        assertEquals("doStartTag() did not return SKIP_BODY.",
                Tag.SKIP_BODY, tag.doStartTag());
        assertEquals("doEndTag() did not return EVAL_PAGE.",
                Tag.EVAL_PAGE, tag.doEndTag());
        
        verifyOutput((String) request.getAttribute(REQUEST_TEST));
    }

    /**
     * @see org.devyant.decorutils.tags.decorator.AbstractDecoratorTaglibTestCase#newTagInstance()
     */
    protected final DecorateTag newTagInstance() {
        return new DecorateAndStoreTag();
    }
}
