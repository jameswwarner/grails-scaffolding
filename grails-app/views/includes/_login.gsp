<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<div id=login-box-wrapper class=${((!session.userId && session.requireLogin) || splash) ? '' : 'hide-me'}>
    <div class="login-box">
        <div id=signin-facebook>
            <div class=sign-in>
                <div class=facebook-login>Connect with Facebook</div>
            </div>
            <div id=email-instead>
                <p>Rather use email?</p>
                <div><span class="faux-link email-switch">Sign in with email</span></div>
                <div><g:link controller="signup">New member?</g:link></div>
            </div>
        </div>
        <div id=signin-email class=hide-me>
            <div id=signin-error></div>
            <label>Email
                <input type=text id=email-input>
            </label>
            <label>Password
                <input type=password id=password-input>
            </label>
            <div id=email-signin-submit>Sign in</div>
            <div class=signin-alt>
                <p>Not a member?</p>
                <g:link controller="signup">Sign up</g:link>
            </div>
            <div class=signin-alt>
                <p>Forgot your password?</p>
                <span id=forgot-password class=faux-link>Click here</span>
            </div>
            <div class=signin-alt>
                <p>Rather use Facebook?</p>
                <div class=sign-in>
                    <div class=facebook-login>Sign In with Facebook</div>
                </div>
            </div>
        </div>
        <div id=signin-forgot class=hide-me>
            <div id=forgot-message></div>
            <p>Forgot your password?</p>
            <label>Email
                <input type=text id=forgot-email>
            </label>
            <div id=forgot-submit>Submit</div>
            <div><span class="faux-link email-switch">Sign in</span></div>
        </div>
    </div>
</div>
