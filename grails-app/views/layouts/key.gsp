<!doctype html>
<!-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved -->
<g:render template="/includes/htmlTag" />
<head>
    <g:render template="/includes/meta" />
    <title>
        Scaffold &bull;
        Official Site
    </title>
    <r:require module="site" />
    <r:require module="uservoice" />
    <link rel=icon             href="${resource(dir: 'images/key', file: 'bookmark.png')}">
    <link rel=apple-touch-icon href="${resource(dir: 'images/key', file: 'symbol-129x129.png')}">
    <link rel=stylesheet       href="//fonts.googleapis.com/css?family=Lato:100,300,400,700,900">
    <link rel=stylesheet       href="//fonts.googleapis.com/css?family=Old+Standard+TT:400italic">
    <g:layoutHead/>
    <r:layoutResources />
    <ga:trackPageview />
    <g:if env="production">
        <script>
            (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
            (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
            m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
            })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
            ga('create', 'UA-99999999-9', 'scaffold.com');
            ga('send', 'pageview');
        </script>
    </g:if>
    <g:render template="/includes/fixIE" />
</head>
<body id="${pageProperty(name: 'body.id') ?: 'page-default'}">

<div id=fb-root></div>
<div id=section-header-wrapper><g:render template="/includes/header" /></div>
<div id=section-body-wrapper><g:layoutBody /></div>
<div id=section-footer-wrapper><g:render template="/includes/footer" /></div>

<g:render template="/includes/base" />
<r:layoutResources />
</body>
</html>
