<%@ page import="org.devyant.decorutils.tags.decorator.IterateAndDecorateTag" %>
<%@ taglib uri="/WEB-INF/tld/decorutils-decorator.tld" prefix="deco" %>

<% IterateAndDecorateTag tag = (IterateAndDecorateTag) request.getAttribute("tag"); %>

<deco:iterateAndDecorate
	id="x"
	name="<%= tag.getName() %>"
	property="<%= tag.getProperty() %>"
	decorator="<%= tag.getDecorator() %>"
	attributes="<%= tag.getAttributes() %>"
	><strong><%= x %></strong></deco:iterateAndDecorate>