<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<html>
<head>
    <title>Reset Password</title>
    <meta name="layout" content="key">
</head>
<body id="page-reset-password">
<g:render template="/includes/subNav" />

<h1>Reset Password</h1>
<div class=content-box>

<g:if test="${user}">

    <label>New password
        <input type=password id=user-new-password>
    </label>
    <div id=save-password-button>Reset Password</div>
    <input id=reset-token type=hidden value=${user.passwordResetToken}>
    <div id=password-reset class=hide-me>
        Password reset.
        <g:link controller="page" action="gateway">Login Now</g:link>
    </div>
</g:if>
<g:else>
    Sorry, invalid attempt to reset password.
</g:else>

</div>

</body>
</html>