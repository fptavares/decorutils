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
import org.devyant.decorutils.xml.DynaNode;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.util.Iterator;

/**
 * This tag is meant to transform simple information stored on a XML
 * document. There is no decorator involved for this is only meant to
 * provide simple iteration through a document's child nodes. You may
 * select the attibutes you wish to manipulate and use them for
 * whatever purpose you want within the body of this tag.
 * <p>
 * Each <code>XNestedTag</code> nested tag refers to the corresponding
 * nested nodes.
 * </p>
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 1/Fev/2005 2:44:44
 */
public class XNestedTag extends BaseXTag {
    /**
     * The items <code>Iterator</code>.
     */
    private Iterator items;
    /**
     * This id can be used to avoid a clash between the attributes
     * names in the scope. The attributes will be set as [id]_[attribute].
     */
    private String id = "";

    /**
     * Getter method for the id attribute.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * This id can be used to avoid a clash between the attributes
     * names in the scope. The attributes will be set as [id]_[attribute].
     *
     * @param id The id used as prefix
     */
    public void setId(String id) {
        this.id = id + "_";
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
        BaseXTag parent = (BaseXTag) findAncestorWithClass(this, BaseXTag.class);

        if (parent == null) {
            throw new InvalidTagPlacementException("XNestedTag");
        }

        try {
            final DynaNode node = parent.getCurrent();

            items = node.getChildren().iterator();

            if (items == null) {
                return SKIP_BODY;
            }
        } catch (NullPointerException e) {
            return SKIP_BODY;
        }

        if (getAttributesCollection() == null) {
            setAttributesCollection(parent.getAttributesCollection());
        }

        return eval();
    }

    /**
     * @return Return constant
     */
    private int eval() {
        try {
            if (items.hasNext()) {
                DynaNode node = (DynaNode) items.next();
    
                doEvalCommon(node);
    
                if (getAttributesCollection() != null) {
                    for (Iterator i = getAttributesCollection().iterator(); i.hasNext();) {
                        String attr = (String) i.next();
                        pageContext.setAttribute(id + attr, node.get(attr));
                    }
                }
    
                return EVAL_BODY_BUFFERED;
            } else {
                return SKIP_BODY;
            }
        } catch (Throwable e) {
            e.printStackTrace();
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
            throw new JspTagException("XNestedTag: " + e.getMessage());
        }

        // clear up so the next time the body content is empty
        body.clearBody();

        return eval();
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() {
        // clean attributes set to pageContext
        if (getAttributesCollection() != null) {
            for (Iterator i = getAttributesCollection().iterator(); i.hasNext();) {
                pageContext.removeAttribute(id + String.valueOf(i.next()));
            }
        }

        return EVAL_PAGE;
    }
}
