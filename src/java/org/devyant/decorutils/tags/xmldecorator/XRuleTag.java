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

import org.devyant.decorutils.Utils;
import org.devyant.decorutils.exceptions.InvalidRuleFormatException;
import org.devyant.decorutils.exceptions.InvalidTagPlacementException;
import org.devyant.decorutils.xml.DynaNode;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.util.Collection;

/**
 * This tag enables you to specify rules for the evaluation
 * or not of it's body.
 * <p>
 * These rules should be separated by semi-colons (';')
 * which will equivelent to an OR operation. These rules should be
 * written in the form: [attribute]=[value] or [attribute]!=[value].
 * </p>
 *
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 3/Fev/2005 18:54:29
 */
public class XRuleTag extends RuledTag {
    /**
     * The rules.
     */
    private Collection rules = null;

    /**
     * @param rules A set of rules seperated by semi-colons
     */
    public final void setRules(final String rules) {
        this.rules = Utils.splitToCollectionIgnoreSpace(rules, ';');
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
        BaseXTag parent = (BaseXTag) findAncestorWithClass(this, BaseXTag.class);

        if (parent == null) {
            throw new InvalidTagPlacementException(getClass().getName());
        }

        final DynaNode node = parent.getCurrent();
        boolean evaluateRules = true;

        /**
         * case: "*"
         */
        if (rules.size() == 1) {
            String rule = String.valueOf(rules.iterator().next());
            if (rule.equals("*")) {
                if (parent.isGoRuleElse()) {
                    evaluateRules = false;     // evaluate body
                } else {
                    return SKIP_BODY;          // skip body
                }
            }
        }

        /**
         * case: plain normal
         */
        if (evaluateRules) {
            // verify rules
            boolean verifies = false;
            try {
                verifies = node.verifiesRules(rules);
            } catch (InvalidRuleFormatException e) {
                throw new JspException("XRuleTag: " + e.getMessage());
            }

            if (verifies) {
                parent.setGoRuleElse(false); // else is de-activated
            } else {
                return SKIP_BODY;
            }
        }

        return EVAL_BODY_BUFFERED;
    }

    /**
     * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
     */
    public int doAfterBody() throws JspTagException {
        BodyContent body = getBodyContent();
        try {
            body.writeOut(getPreviousOut());
        } catch (IOException e) {
            throw new JspTagException("XRuleTag: " + e.getMessage());
        }

        // clear up so the next time the body content is empty
        body.clearBody();

        return SKIP_BODY;
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() {
        return EVAL_PAGE;
    }
}
