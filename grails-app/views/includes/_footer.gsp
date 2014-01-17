<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<div id=section-footer class=float-endgame>
    <div id=footer-left>&copy; 2013-2014 Scaffold</div>
    <div id=footer-middle>
        <g:ifEnabled>
        <ul class=nav-bar-light>
            <li><g:link controller="page" action="about">About</g:link></li>
            <li><g:link controller="page" action="contact">Contact</g:link></li>
            <li><g:link controller="page" action="team">Team</g:link></li>
        </ul>
        <ul class=nav-bar-light>
            <li><g:link controller="page" action="privacy">Privacy</g:link></li>
            <li><g:link controller="page" action="terms">Terms</g:link></li>
            <%-- li><g:link controller="page" action="jobs">Jobs</g:link></li --%>
            <%-- li><g:link controller="page" action="investors">Investors</g:link></li --%>
        </ul>
        </g:ifEnabled>&nbsp;
    </div>
    <div id=footer-right>San Francisco</div>
</div>

<g:ifAdmin>
    <div id=hub-link>
        <g:link mapping="admin" controller="mktg-hub">Marketing Hub</g:link>
    </div>
</g:ifAdmin>

<g:if env="development">
    <div class=info-rollover-area>.
        <div class=info-rollover-msg style="background-color: pink;">
          <p>Token &rarr; ${request.token}</p>
        </div>
    </div>
</g:if>
