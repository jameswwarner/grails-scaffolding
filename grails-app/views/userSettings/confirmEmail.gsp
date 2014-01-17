<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<html>
<head>
    <title>Email Confirmation</title>
    <meta name="layout" content="key">
</head>
<body id="page-email-confirm">
<g:render template="/includes/subNav" />

<h1>Email Confirmation</h1>
<div class=content-box>

<g:if test="${emailConfirmed}">
    Congrats, now the fun part starts.
    <div>
    <g:link controller="browse"><div id=start-collecting-button>Start Collecting</div></g:link>
    </div>
</g:if>
<g:else>
    Sorry, we could not find your email address.
</g:else>

</div>

</body>
</html>