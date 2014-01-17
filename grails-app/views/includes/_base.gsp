<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<g:img id="loading-image" class="hide-me" uri="/images/key/loading.gif" alt="loading..." />
<g:render template="/includes/unsupportedBrowser" />

<div id=screen-dim class=${(!session.userId && session.requireLogin) ? '' : 'hide-me'}></div>

<g:render template="/includes/login" />

<input id=base-uri    type=hidden value="${resource(dir: '/')}">  <%-- used by library.action.call --%>
<input id=facebook-id type=hidden value="${g.facebookId()}">
<input id=is-mobile-session type=hidden value="${session.isMobile}">
<g:if env="production">
    <input id=env-production type=hidden value=true>
</g:if>
