// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

'use strict';

//Naming notes:
//  app.module.display(data) --> takes data object and updates ui
//  app.module.update()      --> makes server call to get data and setup display callback
//  app.module.setup()       --> module initialization

if (!app)
   var app = {};

app.login = {
    handleFbSuccess: function(authResponse) {
        //debug(authResponse);
        var code = library.browser.getUrlVariables()['code'];
        library.action.call('auth', { action: 'login-facebook',
            callback: app.login.displayFbLogin,
            params: { accessToken: authResponse.accessToken, expiresIn: authResponse.expiresIn,
               signedRequest: authResponse.signedRequest, userID: authResponse.userID,
               code: code } });
    },
    fbLogin: function() {
        FB.login(function(response) {
            if (response.authResponse) {
                // connected
                app.login.handleFbSuccess(response.authResponse);
            } else {
                library.loading.off();
                // cancelled, do nothing
            }
        }, {scope: 'email,user_likes,publish_actions'});
    },
    displayFbLogin: function(data) {
        if (!data.error) {
            if (data.enabled && !data.signupCompleted) {
                // they're ready, but need the signup flow
                library.ui.refresh(library.action.getBaseUri() + 'signup');
            }
            else /*if (data.enabled)*/ {
                if ($('#page-welcome').exists()) {
                    var dest = $('#redirect-dest').val();
                    if (dest) {
                        library.ui.refresh(dest);
                    }
                    else {
                        library.ui.refresh(library.action.getBaseUri());
                    }
                }
                else {
                    // they're logged in, let's just refresh this page
                    library.ui.refresh();
                }
            }
        }
    },
    handleFbLoginClick: function() {
        library.loading.on(true);
        // first check if already authenticated
        FB.getLoginStatus(function(response) {
            if (response.status === 'connected') {
                // connected
                app.login.handleFbSuccess(response.authResponse);
            } else {
                // not_logged_in
                app.login.fbLogin();
            }
        });
    },
    displayLogout: function(data) {
        if (!data.error) {
            library.ui.refresh(library.action.getBaseUri());
        }
    },
    logout: function() {
        library.action.call('auth', { action: 'logout',
            callback: app.login.displayLogout });
    },
    showLoginFlyover: function() {
        if ($('#is-mobile-session').val() == "true") {
            library.ui.refresh(library.action.getBaseUri() + "welcome");
        }
        else {
            $('#screen-dim, #login-box-wrapper').fadeIn();
        }
    },
    switchToEmailSignin: function() {
        $('#signin-facebook').hide();
        $('#signin-forgot').hide();
        $('#signin-email').fadeIn();
    },
    showForgotPassword: function() {
        $('#signin-email').hide();
        $('#signin-forgot').fadeIn();
    },
    displayEmailLogin: function(data, options) {
        if (data.error) {
            $('#signin-error').html('Invalid username/password. Please try again.');
        }
        else {
            app.login.displayFbLogin(data);
        }
    },
    handleEmailSigninClick: function() {
        var email    = $('#email-input').val();
        var password = $('#password-input').val();
        library.action.call('auth', { action: 'email-login',
            callback: app.login.displayEmailLogin,
            params: { email: email, password: password } });
    },
    updateForgotPassword: function() {
        var email = $('#forgot-email').val();
        library.action.call('auth', { action: 'forgot-password',
            params: { email: email } });
        $('#forgot-message').html('If an account with this email has been created, we have sent an email to this address with reset instructions.');
    },
    setup: function() {
        library.ui.liveClick('.facebook-login',      this.handleFbLoginClick);
        library.ui.liveClick('.action-signout',      this.logout);
        library.ui.liveClick('#signin-link',         this.showLoginFlyover);
        library.ui.liveClick('#signin-avatar',       this.showLoginFlyover);
        library.ui.liveClick('.email-switch',        this.switchToEmailSignin);
        library.ui.liveClick('#email-signin-submit', this.handleEmailSigninClick);
        library.ui.liveClick('#forgot-password',     this.showForgotPassword);
        library.ui.liveClick('#forgot-submit',       this.updateForgotPassword);
    }
};
