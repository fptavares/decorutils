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

import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * <i>Else</i> is activated on each iteration
 * and deactivated when a rule has been verified.
 *
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 3/Fev/2005 23:29:08
 */
public class RuledTag extends BodyTagSupport {
    /**
     * <i>Else</i> is activated on each iteration
     * and deactivated when a rule has been verified.
     */
    private boolean goRuleElse = true;
    
    /**
     * @return Returns the goRuleElse.
     */
    public final boolean isGoRuleElse() {
        return goRuleElse;
    }
    /**
     * @param goRuleElse The goRuleElse <code>boolean</code> to set.
     */
    public final void setGoRuleElse(final boolean goRuleElse) {
        this.goRuleElse = goRuleElse;
    }
}
