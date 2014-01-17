<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<html>
<head>
    <title>User Settings</title>
    <meta name="layout" content="key">
</head>
<body id="page-settings">
<g:render template="/includes/subNav" model="[pageName:'settings']" />

<h1>User Settings</h1>
<div class=content-box>

    <div id=email-settings>
        <h2>Email Settings</h2>
        <div class=settings-item data-field=disableFollowEmails>
            <label>New follower emails enabled.</label>
            <input type=radio name=follower-email-control value=false id=follower-email-control-yes> Yes</input>
            <input type=radio name=follower-email-control value=true id=follower-email-control-no> No</input>
        </div>
        <div class=settings-item data-field=disableCommentEmails>
            <label>New message emails enabled.</label>
            <input type=radio name=message-email-control value=false id=message-email-control-yes> Yes</input>
            <input type=radio name=message-email-control value=true id=message-email-control-no> No</input>
        </div>
        <div class=settings-item data-field=disableNewsfeedEmails>
            <label>Newsfeed emails enabled.</label>
            <input type=radio name=newsfeed-email-control value=false id=newsfeed-email-control-yes> Yes</input>
            <input type=radio name=newsfeed-email-control value=true id=newsfeed-email-control-no> No</input>
        </div>
        <div class=settings-item data-field=disableMarketingEmails>
            <label>Trending and contest update emails enabled.</label>
            <input type=radio name=trending-email-control value=false id=trending-email-control-yes> Yes</input>
            <input type=radio name=trending-email-control value=true id=trending-email-control-no> No</input>
        </div>
        <span id=unsubscribe-all class=faux-link>Unsubscribe from all emails</span>

        <p class=space-above><small>These settings are automatically saved.</small></p>
    </div>

</div>

</body>
</html>
