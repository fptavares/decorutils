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

import org.devyant.decorutils.exceptions.InvalidTagPlacementException;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;

/**
 * Outputs the value of an attribute from the parent "X-Tag".
 *
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 24/Fev/2005 19:44:43
 */
public class XAttributeTag extends TagSupport {
    /**
     * The name of the attribute to fetch.
     */
    private String attribute;

    /**
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
        final BaseXTag parent = (BaseXTag) findAncestorWithClass(this, BaseXTag.class);

        if (parent == null) {
            throw new InvalidTagPlacementException("XAttributeTag");
        }

        try {
            pageContext.getOut().print(parent.getCurrent().get(attribute));
        } catch (IOException e) {
            throw new JspTagException(this.getClass().getName()
                    + ": " + e.getMessage());
        }

        return SKIP_BODY;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() {
        return EVAL_PAGE;
    }
    
    
    /**
     * @param attribute The attribute <code>String</code> to set.
     */
    public final void setAttribute(final String attribute) {
        this.attribute = attribute;
    }
}
