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

import org.devyant.decorutils.xml.DynaNode;
import org.devyant.decorutils.xml.SimpleXmlWrapper;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.IterationTag;
import java.io.IOException;
import java.util.Iterator;

/**
 * This tag is meant to transform simple information stored on a XML
 * document. There is no decorator involved for this is only meant to
 * provide simple iteration through a document's child nodes. You may
 * select the attibutes you wish to manipulate and use them for
 * whatever purpose you want within the body of this tag.
 *
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 1/Fev/2005 2:44:44
 */
public class XDecorateTag extends BaseXTag implements IterationTag {
    /**
     * The items <code>Iterator</code>.
     */
    private Iterator items;
    /**
     * The document's path.
     */
    private String doc;
    /**
     * The xpath <code>String</code>.
     */
    private String xpath = null;

    /**
     * @param doc XML document you wish to decorate.
     */
    public final void setDocument(final String doc) {
        this.doc = doc;
    }

    /**
     * Xpath for the nodes to be selected.
     * @param xpath The xpath
     */
    public final void setXpath(final String xpath) {
        this.xpath = xpath;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspTagException {
        // retrieve items
        try {
            SimpleXmlWrapper xml;
            if (doc.startsWith("http")) {
                xml = new SimpleXmlWrapper(doc);
            } else {
                xml = new SimpleXmlWrapper(pageContext.getServletConfig()
                        .getServletContext().getRealPath(doc));
            }
            // if xpath is set, use it
            if (xpath == null) {
                items = xml.getNodes().iterator();
            } else {
                items = xml.getNodes(xpath).iterator();
            }
        } catch (Exception e) {
            throw new JspTagException(
                    "XDecorateTag (while setting document as: " + doc + "): "
                    + e.getMessage());
        }

        if (items == null) {
            return SKIP_BODY;
        }
        return eval();
    }

    /**
     * @return Return constant
     */
    private int eval() {
        if (items.hasNext()) {
            DynaNode node = (DynaNode) items.next();

            doEvalCommon(node);

            if (getAttributesCollection() != null) {
                for (Iterator i = getAttributesCollection().iterator(); i.hasNext();) {
                    final String attr = String.valueOf(i.next());
                    pageContext.setAttribute(attr, node.get(attr));
                }
            }

            return EVAL_BODY_BUFFERED;
        } else {
            return SKIP_BODY;
        }
    }

    /**
     * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
     */
    public int doAfterBody() throws JspTagException {
        BodyContent body = getBodyContent();
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
        // clean attributes set to pageContext
        if (getAttributesCollection() != null) {
            for (Iterator i = getAttributesCollection().iterator(); i.hasNext();) {
                pageContext.removeAttribute(String.valueOf(i.next()));
            }
        }

        return EVAL_PAGE;
    }
}
