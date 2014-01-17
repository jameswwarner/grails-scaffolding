// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

'use strict';

//Naming notes:
//  app.module.display(data) --> takes data object and updates ui
//  app.module.update()      --> makes server call to get data and setup display callback
//  app.module.setup()       --> module initialization

if (!app)
   var app = {};

app.userSettings = {
    updateBooleanSetting: function(event) {
        var settingsElem = $(event.target).closest('.settings-item');
        var field = settingsElem.data('field');
        var value = settingsElem.find('input:radio:checked').val();
        var params = {};
        params[field] = value;
        library.action.call('user', { action: 'update', params: params,
            callback: app.userSettings.displayCurrentSettings });
    },
    setup: function() {
        if ($('#page-settings').exists()) {
        }
    }
};

app.userSecurity = {
    displaySavePassword: function(data) {
        if (!data.error) {
            $('#password-reset').fadeIn();
        }
    },
    saveResetPassword: function() {
        var password = $('#user-new-password').val();
        var token = $('#reset-token').val();
        library.action.call('auth', { action: 'reset-password', params: {token: token, password: password},
            callback: app.userSecurity.displaySavePassword });
    },
    displayPasswordChange: function(data) {
        if (data.error) {
            $('#password-message').text('Invalid password.');
        }
        else {
            $('#password-message').text('Password updated.');
        }
    },
    changePassword: function() {
        if ($('#user-new-password').val() != $('#user-repeat-password').val()) {
            $('#password-message').text('Passwords do not match.');
        }
        else {
            var params = {newPassword: $('#user-new-password').val(), oldPassword: $('#user-current-password').val()};
            library.action.call('auth', { action: 'change-password', params: params,
                callback: app.userSecurity.displayPasswordChange });
        }
    },
    setup: function() {
        if ($('#page-reset-password').exists()) {
            library.ui.liveClick('#save-password-button', this.saveResetPassword);
        }
        if ($('#page-change-password').exists()) {
            library.ui.liveClick('#save-password-button', this.changePassword);
            $('#select-password').addClass('select-active');
        }
    }
};

