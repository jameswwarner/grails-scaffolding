//Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

'use strict';

$(function() {
    library.fixIE.setup();
    library.ui.setup();
    library.dropDown.setup();
    library.infoRollover.setup();
    library.imageSelector.setup();
    library.dialog.setup();
    library.expandable.setup();
    library.bubbleHelp.setup();
    app.page.setup();
    app.userSettings.setup();
    app.userSecurity.setup();
    app.gateway.setup();
    app.login.setup();
    app.facebook.setup();
    app.testMode.setup();
    $().UItoTop({ easingType: 'easeOutQuart' }); // add a scroll to top button
});
