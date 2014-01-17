<!doctype html>
<!-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved -->
<g:render template="/includes/htmlTag" />
<head>
    <meta charset="utf-8">
    <title>Scaffold &bull; <g:layoutTitle /></title>
    <r:require module="admin" />
    <r:layoutResources />
    <link rel=icon             href="${resource(dir: 'images/key', file: 'bookmark.png')}" type="image/png">
    <link rel=apple-touch-icon href="${resource(dir: 'images/key', file: 'symbol-129x129.png')}" type="image/png">
    <link rel=stylesheet       href="//fonts.googleapis.com/css?family=Chango">
    <g:layoutHead />
</head>
<body id="${pageProperty(name: 'body.id') ?: 'page-default'}">

<g:render template="/includes/adminHeader" />
<div id=admin-body><g:layoutBody /></div>
<g:render template="/includes/adminFooter" />

<g:render template="/includes/base" />
<r:layoutResources />
</body>
</html>
