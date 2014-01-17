<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<g:set var="pageType" scope="request">Marketing Hub</g:set>
<html>
<head>
    <r:require module="fileuploader" />
    <meta name="layout" content="admin">
    <title>${pageType}</title>
</head>
<body id="page-hub-mktg">

<div id=mktg-hub-tab-nav>
    <ul>
        <li id=mktg-hub-tab-cfg><g:link           mapping="admin" controller="mktg-hub" action="page" params="[tab: 'configuration']">Configuration</g:link></li>
        <li id=mktg-hub-tab-image-library><g:link mapping="admin" controller="mktg-hub" action="page" params="[tab: 'image-library']">Image Library</g:link></li>
    </ul>
</div>

<g:render template="adminImgDialog" />
</body>
</html>
