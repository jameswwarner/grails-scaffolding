<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<html>
<head>
    <meta name="layout" content="plain">
</head>
<body>

<h1>Images</h1>
<h2>${folderName}</h2>
<div>
    <g:each in="${images}">
        <p>${it}<br><g:img style="max-width: 100px;" uri="${it}" /></p>
    </g:each>
</div>

</body>
</html>
