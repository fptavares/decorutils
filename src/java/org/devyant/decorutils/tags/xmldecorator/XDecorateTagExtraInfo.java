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

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * Implementation of TagExtraInfo for the decorate tag, identifying
 * the scripting object(s) to be made visible.
 *
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 16/12/2004 3:28:52
 */
public class XDecorateTagExtraInfo extends TagExtraInfo {
    /**
     * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(javax.servlet.jsp.tagext.TagData)
     */
    public final VariableInfo[] getVariableInfo(final TagData data) {
        if (data.getAttributeString("attributes") == null) {
            return null;
        }
        final String [] attr = data.getAttributeString("attributes")
            .trim().split("\\s*,+\\s*");
        VariableInfo [] info = new VariableInfo[attr.length];
        
        for (int i = 0; i < attr.length; i++) {
            info[i] = new VariableInfo(attr[i],
                    "java.lang.String",
                    true,
                    VariableInfo.NESTED);
        }
        
        return info;
    }
}
