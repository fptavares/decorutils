<%@ taglib uri="/WEB-INF/tld/decorutils-xmldecorator.tld" prefix="xdeco" %>

<xdeco:xdecorate attributes="name" document="/xTagTest.xml" xpath="/*/*[@name='xTest']">
	<h1><xdeco:xattribute attribute="this.name" /></h1>
	<h1><xdeco:xattribute attribute="this.childrenCount" /></h1>
	<h1><%= name %></h1>
	<xdeco:xnested attributes="description">
		<h2><xdeco:xattribute attribute="this.name" /></h2>
		<h2><xdeco:xattribute attribute="this.childrenCount" /></h2>
		<h2><xdeco:xattribute attribute="name" /></h2>
		<h2><%= description %></h2>
		<xdeco:xrule rules="this.name=yeh; name=hello">
			<h2><i>this.name=yeh; name=hello</i></h2>
		</xdeco:xrule>
		<xdeco:xnested id="third" attributes="description">
			<h3><xdeco:xattribute attribute="this.name" /></h3>
			<h3><xdeco:xattribute attribute="this.childrenCount" /></h3>
			<h3><%= third_description %></h3>
		</xdeco:xnested>
	</xdeco:xnested>
</xdeco:xdecorate>