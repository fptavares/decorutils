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

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.cactus.JspTestCase;
import org.apache.cactus.WebResponse;

/**
 * XTagTest is a <b>cool</b> class.
 * 
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 20/Mar/2005 19:19:37
 */
public class XTagTest extends JspTestCase {
    /**
     * The EXPECTED <code>String</code>.
     */
    private static final String EXPECTED = "\r\n"
            + "\r\n"
            + "\r\n"
            + "    <h1>test</h1>\r\n"
            + "    <h1>4</h1>\r\n"
            + "    <h1>xTest</h1>\r\n"
            + "    \r\n"
            + "        <h2>item</h2>\r\n"
            + "        <h2>1</h2>\r\n"
            + "        <h2>hello</h2>\r\n"
            + "        <h2>Hello World!</h2>\r\n"
            + "        \r\n"
            + "            <h2><i>this.name=yeh; name=hello</i></h2>\r\n"
            + "        \r\n"
            + "        \r\n"
            + "            <h3>description</h3>\r\n"
            + "            <h3>0</h3>\r\n"
            + "            <h3>null</h3>\r\n"
            + "        \r\n"
            + "    \r\n"
            + "        <h2>stuff</h2>\r\n"
            + "        <h2>0</h2>\r\n"
            + "        <h2>null</h2>\r\n"
            + "        <h2>null</h2>\r\n"
            + "        \r\n"
            + "        \r\n"
            + "    \r\n"
            + "        <h2>item</h2>\r\n"
            + "        <h2>2</h2>\r\n"
            + "        <h2>hello2</h2>\r\n"
            + "        <h2>null</h2>\r\n"
            + "        \r\n"
            + "        \r\n"
            + "            <h3>item</h3>\r\n"
            + "            <h3>0</h3>\r\n"
            + "            <h3>null</h3>\r\n"
            + "        \r\n"
            + "            <h3>item</h3>\r\n"
            + "            <h3>2</h3>\r\n"
            + "            <h3>Hello World!... again</h3>\r\n"
            + "        \r\n"
            + "    \r\n"
            + "        <h2>yeh</h2>\r\n"
            + "        <h2>1</h2>\r\n"
            + "        <h2>null</h2>\r\n"
            + "        <h2>null</h2>\r\n"
            + "        \r\n"
            + "            <h2><i>this.name=yeh; name=hello</i></h2>\r\n"
            + "        \r\n"
            + "        \r\n"
            + "            <h3>doubleYeh</h3>\r\n"
            + "            <h3>0</h3>\r\n"
            + "            <h3>null</h3>\r\n"
            + "        \r\n"
            + "    \r\n"
            + "";
    
    /**
     * @throws ServletException {@link PageContext#include(java.lang.String)}
     * @throws IOException  {@link BodyContent#print()}
     */
    public final void testXTag()
        throws ServletException, IOException {
        pageContext.include("/xTagTest.jsp");
    }
    /**
     * @param response The response
     */
    public final void endXTag(final WebResponse response) {
        
        assertEquals("XTag did not output correctly.",
                filter(EXPECTED), filter(response.getText()));
    }
    
    /**
     * @param string The string to apply filter
     * @return The filtered string
     */
    private String filter(final String string) {
        return string.trim()
            .replaceAll("[\\r\\n]", "").replaceAll("\\t", "    ");
    }
}
