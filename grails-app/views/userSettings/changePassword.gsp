<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<html>
<head>
    <title>Change Password</title>
    <meta name="layout" content="key">
</head>
<body id="page-change-password">
<g:render template="/includes/subNav" model="[pageName:'settings']" />

<h1>Change Password</h1>
<div class=content-box>

    <div id=password-message>
    </div>

    <label>Current password
        <input type=password id=user-current-password>
    </label>
    <label>New password
        <input type=password id=user-new-password>
    </label>
    <label>Repeat password
        <input type=password id=user-repeat-password>
    </label>
    <div id=save-password-button>Change Password</div>

</div>

</body>
</html>