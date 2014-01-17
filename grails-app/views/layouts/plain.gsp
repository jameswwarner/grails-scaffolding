<!doctype html>
<!-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved -->
<g:render template="/includes/htmlTag" />
<head>
    <g:render template="/includes/meta" />
    <title>Scaffold</title>
    <r:require module="site" />
    <link rel=icon       href="${resource(dir: 'images/key', file: 'bookmark.png')}">
    <link rel=stylesheet href="//fonts.googleapis.com/css?family=Lato:100,300,400,700,900">
    <g:layoutHead/>
    <r:layoutResources />
    <ga:trackPageview />
</head>

<body id="${pageProperty(name: 'body.id') ?: 'page-plain'}">
<g:layoutBody />

<g:render template="/includes/base" />
<r:layoutResources />
</body>
</html>
