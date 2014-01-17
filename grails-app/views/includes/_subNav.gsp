<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<div class=subnav-outer-wrapper>
    <div id=subnav-bg></div>
    <div id=subnav-bar>
        <div id=header-search>
            <g:img class="search-action" uri="/images/key/theme1/icon-search.png" alt="Search" />
            <input class="search-query no-auto-focus" type=text placeholder="Search products">
        </div>

        <g:if test="${pageName == 'example'}">
            <ul id=example-selections class=subnav-menu>
                <li id=select-subpage data-subpage=my-subpage>
                   Example submenu
                </li>
            </ul>
        </g:if>
        <g:elseif test="${pageName == 'gateway'}">
            <g:ifEnabled>
                <ul id=gateway-selections class=subnav-menu>
                    <li id=select-gateway-subpage>Subpage</li>
                </ul>
            </g:ifEnabled>
        </g:elseif>

        <ul class=nav-bar-small>
            <li class=drop-down>
                <g:img uri="/images/key/theme1/symbol-settings-rest.png" />
                <g:img uri="/images/key/theme1/symbol-settings-rollover.png" />
                <ul class=drop-down-choices>
                    <g:ifEnabled>
                        <li><span class=action-signout>Sign out</span></li>
                    </g:ifEnabled>
                    <li><g:link controller="page" action="about">About</g:link></li>
                    <li><g:link controller="page" action="contact">Contact</g:link></li>
                    <li><g:link controller="page" action="team">Team</g:link></li>
                    <%-- li><g:link controller="page" action="jobs">Jobs</g:link></li --%>
                    <%-- li><g:link controller="page" action="investors">Investors</g:link></li --%>
                    <li><g:link controller="page" action="privacy">Privacy</g:link></li>
                    <li><g:link controller="page" action="terms">Terms</g:link></li>
                </ul>
            </li>
        </ul>
    </div>
</div>

