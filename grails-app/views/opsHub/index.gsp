<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<g:set var="pageType" scope="request">Operations Hub</g:set>
<html>
<head>
    <meta name="layout" content="admin">
    <title>${pageType}</title>
</head>
<body id="page-hub-ops">

<div id=ops-hub-tab-nav>
    <ul>
        <li id=ops-hub-tab-site-cfg><g:link    mapping="admin" controller="ops-hub" action="page" params="[tab: 'site-cfg']">Site Configuration</g:link></li>
        <li id=ops-hub-tab-status-info><g:link mapping="admin" controller="ops-hub" action="page" params="[tab: 'status-info']">Status Information</g:link></li>
        <li id=ops-hub-tab-user-mgmt><g:link   mapping="admin" controller="ops-hub" action="page" params="[tab: 'user-mgmt']">User Management</g:link></li>
        <li id=ops-hub-tab-rest-tool><g:link   mapping="admin" controller="ops-hub" action="page" params="[tab: 'rest-tool']">REST Tool</g:link></li>
    </ul>
</div>

</body>
</html>
