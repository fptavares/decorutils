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
package org.devyant.decorutils.xml;

import java.io.IOException;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 6/Mar/2005 5:06:26
 */
public class SimpleXmlWrapperTest extends TestCase {
    /**
     * The DESCRIPTIONS_COUNT <code>int</code>.
     */
    private static final int DESCRIPTIONS_COUNT = 2;
    /**
     * The ROOT_NODES_SIZE <code>int</code>.
     */
    private static final int ROOT_NODES_SIZE = 1;
    /**
     * The ITEM_NODES_SIZE <code>int</code>.
     */
    private static final int ITEM_NODES_SIZE = 1;

    /**
     * @throws SAXException {@link SimpleXmlWrapper#SimpleXmlWrapper(String)}
     * @throws IOException {@link SimpleXmlWrapper#SimpleXmlWrapper(String)}
     * @throws ParserConfigurationException {@link SimpleXmlWrapper}#SimpleXmlWrapper(String)}
     * @throws TransformerException {@link SimpleXmlWrapper#getNodes()}
     */
    public final void testFileLoading() throws ParserConfigurationException,
        IOException, SAXException, TransformerException {
        final SimpleXmlWrapper wrapper = new SimpleXmlWrapper(getClass()
                .getClassLoader().getResource("testFile.xml").toExternalForm());
        //final SimpleXmlWrapper wrapper = new SimpleXmlWrapper("testFile.xml");

        Assert.assertNotNull("Didn't load correctly.", wrapper);

        final Collection rootNodes = wrapper.getNodes();
        final Collection itemNodes = wrapper.getNodes("/*/*");

        Assert.assertNotNull("Null root nodes.", rootNodes);
        Assert.assertNotNull("Null item nodes.", itemNodes);
        Assert.assertEquals("Root nodes size.",
                ROOT_NODES_SIZE, rootNodes.size());
        Assert.assertEquals("Item nodes size.",
                ITEM_NODES_SIZE, itemNodes.size());

        final DynaNode testNode = (DynaNode) itemNodes.iterator().next();

        Assert.assertEquals("Descriptions count.",
                DESCRIPTIONS_COUNT, testNode.getMap().size());
    }

    /**
     * @return The suite
     */
    public static Test suite() {
        return new TestSuite(SimpleXmlWrapperTest.class);
    }
}
