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

import com.sun.org.apache.xpath.internal.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * A simple wrapper for XML documents. Each {@link DynaNode} has a set of
 * attributes which include text data retrieved from child nodes body.
 *
 * @author Filipe Tavares
 * @version $Revision 1.0$ ($Author$)
 * @since 31/Jan/2005 14:22:19
 */
public class SimpleXmlWrapper {
    /**
     * The XML document to wrap.
     */
    private Document doc;

    /**
     * Creates a new <code>SimpleXmlWrapper</code> instance.
     * @param file The XML file
     * @throws ParserConfigurationException Thrown by document factory
     * @throws IOException Thrown by XML parser
     * @throws SAXException Thrown by XML parser
     */
    public SimpleXmlWrapper(final String file)
        throws ParserConfigurationException, IOException, SAXException {
        // read the XML doc
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder =
                factory.newDocumentBuilder();
        InputSource is = new InputSource(file);
        doc = builder.parse(is);
    }

    /**
     * Returns a <code>Collection</code> of the nodes
     * selected by the XPath query.
     * @param xpath XPath query
     * @return <code>Collection</code> of <code>DynaNode</code>'s
     * @throws TransformerException Thrown by the XPath API
     */
    public final Collection getNodes(final String xpath)
        throws TransformerException {
        
        NodeList nodes = XPathAPI.selectNodeList(doc, xpath);
        return convert(null, nodes);
    }

    /**
     * Returns a <code>Collection</code> of all the nodes in the document.
     * @return <code>Collection</code> of <code>DynaNode</code>'s
     * @throws TransformerException Thrown by the XPath API
     */
    public final Collection getNodes() throws TransformerException {
        /*String xpath = "/"+root+"/"+node;

        NodeList nodes = XPathAPI.selectNodeList(doc, xpath);*/

        NodeList nodes = doc.getChildNodes();

        return convert(null, nodes);
    }

    /**
     * Converts the <code>NodeList</code> instance to a <code>Collection</code>
     * of <code>DynaNode</code>'s.
     * @param bean The parent node
     * @param nodes The child nodes
     * @return <code>Collection</code> of <code>DynaNode</code>'s
     */
    private static Collection convert(final DynaNode bean,
            final NodeList nodes) {
        LinkedList list = new LinkedList();

        for (int i = 0; i < nodes.getLength(); i++) {
            final Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {    // element
                DynaNode child = getDynaNode(bean, node);
                list.add(child);
            } else if ((node.getNodeType() == Node.TEXT_NODE) // text node
                    && (bean != null)                         // isn't document
                    && (bean.getParent() != null)             // isn't root node
                    && (!node.getNodeValue().matches("^\\s$"))) { // !whitespace
                bean.getParent()
                    .set(bean.getName(), node.getNodeValue().trim());
            }
        }

        if (list.isEmpty()) {
            return null; // if empty, return null for consistency purposes
        } else {
            return list;
        }
    }

    /**
     * Create a <code>DynaNode</code>.
     * @param parent The parent node
     * @param node The node
     * @return A <code>DynaNode</code> instance
     */
    private static DynaNode getDynaNode(final DynaNode parent,
            final Node node) {
        DynaNode bean = new DynaNode();
        bean.setName(node.getNodeName());
        bean.setParent(parent);

        if (node.hasAttributes()) {
            NamedNodeMap nodeList = node.getAttributes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                bean.set(nodeList.item(i).getNodeName(),
                        nodeList.item(i).getNodeValue());
            }
        }

        if (node.hasChildNodes()) {
            bean.setChildren(convert(bean, node.getChildNodes()));
        }

        return bean;
    }
}
