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

import org.apache.cactus.WebResponse;

/**
 * DecorateTagTest is a <b>cool</b> class.
 * 
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 18/Mar/2005 2:39:13
 */
public class DecorateTagTest extends AbstractDecoratorTaglibTestCase {
    /**
     * The <code>DecorateTag</code> test for decorators.
     * @throws JspException {@link DecorateTag#doStartTag()}
     */
    public final void testDecorators() throws JspException {
        assertEquals("doStartTag() did not return SKIP_BODY.",
                Tag.SKIP_BODY, getTag().doStartTag());
        assertEquals("doEndTag() did not return EVAL_PAGE.",
                Tag.EVAL_PAGE, getTag().doEndTag());
    }
    /**
     * @param response The response
     */
    public final void endDecorators(final WebResponse response) {
        verifyOutput(response.getText());
    }
    
    /**
     * The <code>DecorateTag</code> test for wrappers.
     * @throws JspException {@link DecorateTag#doStartTag()}
     */
    public final void testWrappers() throws JspException {
        getTag().setDecorator(WRAPPER_CLASS);
        getTag().setName(BEAN_KEY + "." + PROPERTY_KEY);
        getTag().setProperty(WRAPPER_PROPERTY);
        
        assertEquals("doStartTag() did not return SKIP_BODY.",
                Tag.SKIP_BODY, getTag().doStartTag());
        assertEquals("doEndTag() did not return EVAL_PAGE.",
                Tag.EVAL_PAGE, getTag().doEndTag());
    }
    /**
     * @param response The response
     */
    public final void endWrappers(final WebResponse response) {
        verifyOutput(response.getText());
    }

    /**
     * @see org.devyant.decorutils.tags.decorator.AbstractDecoratorTaglibTestCase#newTagInstance()
     */
    protected final DecorateTag newTagInstance() {
        return new DecorateTag();
    }
}
