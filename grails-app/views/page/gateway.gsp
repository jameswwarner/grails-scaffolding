<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<html>
<head>
    <meta name="layout" content="key">
    <meta property="og:title" content="Scaffold">
    <%-- meta property="og:image" content="http:${resource(dir: 'images/key', file: 'share-thumb.jpg')}" --%>
    <meta property="og:description" content="${scaffold.Configuration.lookup("og-description")}">
    <meta name="description" content="${scaffold.Configuration.lookup("og-description")}">
</head>
<body id="page-gateway">

<g:render template="/includes/subNav" model="[pageName:'gateway']" />

Hi there.  This is the gateway.

</body>
</html>
