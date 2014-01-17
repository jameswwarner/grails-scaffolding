// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

// facebook stuff
(function(d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) return;
        js = d.createElement(s); js.id = id;
        var fbappid = $('head meta[property="fb:app_id"]').attr('content');
        js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId="+fbappid;
        fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));

// twitter stuff
!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");
