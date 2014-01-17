<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<h2 class=space-below>Status Information</h2>
<div class=block2>
    <h3>System</h3>
    <div>
        <b>Build Time:&nbsp;</b>
        ${buildTime.toString()}
    </div>
</div>
<div class=block3>
    <h3>REST Tests</h3>
    <div id=rest-tests>
        <b id=test-header-template class=hide-me></b>
        <div id=test-url-template class=hide-me><a href=""></a></div>
        <p id=test-result-template class=hide-me>
            <code><span class=advisory>FAILED</span></code>
        </p>
    </div>
</div>
