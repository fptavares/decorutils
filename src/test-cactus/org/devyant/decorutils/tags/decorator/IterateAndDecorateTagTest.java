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


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.cactus.WebResponse;
import org.devyant.decorutils.decorators.DateDecorator;

/**
 * IterateAndDecorateTagTest is a <b>cool</b> class.
 * 
 * @author Filipe Tavares
 * @version $Revision$ ($Author$)
 * @since 18/Mar/2005 2:39:13
 */
public class IterateAndDecorateTagTest extends AbstractDecoratorTaglibTestCase {
    /**
     * The TAG_KEY <code>String</code>.
     */
    private static final String TAG_KEY = "tag";
    /**
     * The EXPECTED_CYCLES <code>int</code>.
     */
    protected static final int EXPECTED_CYCLES = 2;
    /**
     * The REQUEST_TEST <code>String</code>.
     */
    //private static final String REQUEST_TEST = "request_test";
    
    /**
     * The <code>IterateAndDecorateTag</code> test for decorators.
     * @throws ServletException {@link PageContext#include(java.lang.String)}
     * @throws IOException  {@link BodyContent#print()}
     */
    public final void testDecorators()
        throws ServletException, IOException {
        request.setAttribute(TAG_KEY, getTag());
        pageContext.include("/iterateAndDecorateTest.jsp");
    }
    /**
     * @param response The response
     */
    public final void endDecorators(final WebResponse response) {
        commonEnd(response);
    }
    /*public final void testIterateAndDecorateTag()
        throws JspException, IOException {
        
        final IterateAndDecorateTag tag = (IterateAndDecorateTag) getTag();
        tag.setToScope("request");
        tag.setId(REQUEST_TEST);
        tag.setIterate(false); // iteration will external instead of internal

        final String output = "<strong><%=" + REQUEST_TEST + "%></strong>";
        
        doBodyAndIterateTagCommons(tag, output);
    }*/
    
    /**
     * The <code>IterateAndDecorateTag</code> test for wrappers.
     * @throws ServletException {@link PageContext#include(java.lang.String)}
     * @throws IOException  {@link BodyContent#print()}
     */
    public final void testWrappers()
        throws ServletException, IOException {
        getTag().setDecorator(WRAPPER_CLASS);
        getTag().setName(BEAN_KEY + "." + PROPERTY_KEY);
        getTag().setProperty(WRAPPER_PROPERTY);
        
        request.setAttribute(TAG_KEY, getTag());
        pageContext.include("/iterateAndDecorateTest.jsp");
    }
    /**
     * @param response The response
     */
    public final void endWrappers(final WebResponse response) {
        commonEnd(response);
    }
    
    /**
     * @param response The response
     */
    private void commonEnd(final WebResponse response) {
        final Date date = new Date(TIME);
        final Date date2 = new Date(TIME_2);
        final DateFormat format = new SimpleDateFormat(
                DATE_FORMAT, DateDecorator.DEFAULT_LOCALE);
        final String expected = "<strong>" + format.format(date) + "</strong>"
                              + "<strong>" + format.format(date2) + "</strong>";
        assertEquals("IterateAndDecorateTag did not output correctly.",
                expected, response.getText().trim().replaceAll("\n", ""));
    }
    
    /**
     * @param tag The tag
     * @param output The <code>String</code> to insert into the tag's body
     * @throws JspException {@link DecorateTag#doStartTag()}
     * @throws IOException  {@link BodyContent#print()}
     */
    protected final void doBodyAndIterateTagCommons(final BodyTagSupport tag,
            final String output) throws JspException, IOException {
        tag.doStartTag();
        
        // obtain the bodyContent object--presumably doStartTag has returned
        // EVAL_BODY_TAG or EVAL_BODY_BUFFERED.
        final BodyContent bodyContent = this.pageContext.pushBody();
        tag.setBodyContent(bodyContent);
        tag.doInitBody();
        
        // write some "output" into the bodyContent so that endXXX can test it.
        bodyContent.print(output);
        
        // actually handles the processing of the body
        int count = 0;
        do {
            count++;
        } while (tag.doAfterBody() == TagSupport.EVAL_BODY_AGAIN);

        // after the body processing completes
        tag.doEndTag();
        
        // finally call popBody
        this.pageContext.popBody();
        
        // based on setUp we expect EXPECTED_CYCLES repetitions
        assertEquals("The number of repetitions differs from expected one.",
                EXPECTED_CYCLES, count);
    }

    /**
     * @see org.devyant.decorutils.tags.decorator.AbstractDecoratorTaglibTestCase#newTagInstance()
     */
    protected final DecorateTag newTagInstance() {
        return new IterateAndDecorateTag();
    }
}
