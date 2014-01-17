<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<html>
<head>
    <meta name="layout" content="plain">
    <link rel=stylesheet href="${resource(dir: 'css', file: 'errors.css')}">
</head>
<body id="page-error">

<g:if env="development">
    <g:renderException exception="${exception}" />
</g:if>
<h1>Oops!</h1>
<p>Sorry, but it seems an error has occurred.</p>
<p>Return to website:<br>
    <g:link controller="page" action="gateway" class="indent">ourlink.com</g:link>
</p>

</body>
</html>
