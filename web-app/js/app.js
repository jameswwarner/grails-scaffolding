//Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

'use strict';

//Naming notes:
//app.module.display(data) --> takes data object and updates ui
//app.module.update()      --> makes server call to get data and setup display callback
//app.module.setup()       --> module initialization

if (!app)
    var app = {};

app.page = {
    resourceId: null,
    setRealUrl: function() {
        var realLink = $('#real-link').val();
        var option   = $('#param-option').val();
        if (option && realLink) {
            History.pushState(null, null, realLink+'/'+option);
        }
        else if (realLink) {
            History.pushState(null, null, realLink);
        }
    },
    setup: function() {
        this.setRealUrl();
        this.resourceId = $('#resource-id').val();
        library.ui.autoFocus($('#section-body-wrapper'));
    }
};

app.logging = {
    //Usage:
    //   <img src=hover-over-me.jpg class=event-log>
    selector: '.event-log',
    timer: null,
    reset: function() { app.logging.timer = Date.now(); },
    eventHover: function(event) {
        var payload = {
                event: 'hover',
                id: $(event.target).closest(app.logging.selector).attr('id'),
                duration: Date.now() - app.logging.timer
        };
        $.ajax({
            type: 'POST',
            url: library.action.getBaseUri() + 'rest/event-log',
            data: JSON.stringify(payload),
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        });
    },
    setup: function() {
        $(this.selector).hoverIntent({
            over: app.logging.reset,
            timeout: 500,
            out: app.logging.eventHover
        });
    }
};

app.bubbleHelp = {
    //Usage:
    //   <div>Hover Over Me<div class=bubble-help>Help!</div></div>
    //Results in:
    //   <div class=bubble-help-hover>
    //      Hover Over Me
    //      <div class=bubble-wrap>
    //         <div class=bubble-help>Help!</div>
    //         <div>v</div>
    //      </div>
    //   </div>
    elem: null,
    hi: function(event) {
        var hoverElem = $(event.target).closest('.bubble-help-hover');
        app.bubbleHelp.elem = hoverElem.find('.bubble-wrap');
        if (!app.bubbleHelp.elem.exists())
            app.bubbleHelp.elem = hoverElem.find('.bubble-help')
            .wrap('<div class=bubble-wrap></div>')
            .parent().append('<div>&#9660;</div>');  //down triangle
        app.bubbleHelp.elem.find('.bubble-help').show();
        app.bubbleHelp.elem.css('top', -app.bubbleHelp.elem.height())
        .hide().delay('fast').fadeIn();
        if (hoverElem.find('.no-bubble-help:visible').exists())
            app.bubbleHelp.elem.stop(true);
    },
    bye: function() {
        if (app.bubbleHelp.elem)
            app.bubbleHelp.elem.fadeOut('xxslow');
    },
    setup: function(elems) {
        elems = elems ? elems : $('.bubble-help');
        elems.parent().addClass('bubble-help-hover').hoverIntent(this.hi, this.bye);
    }
};

app.reveal = {
    //Usage:
    //   <div class=reveal-msg>Click me<div class=msg-body>Hi!</div></div>
    toggle: function(event) {
        var elem = $(event.target).closest('.reveal-msg');
        if (elem.find('.msg-status').toggleClass('shown').hasClass('shown'))
            elem.find('.msg-body').slideDown();
        else
            elem.find('.msg-body').slideUp();
    },
    setup: function() {
        $('.reveal-msg').click(app.reveal.toggle).find('.msg-body')
        .before($('<span>').addClass('msg-status'));
    }
};


app.help = {
    //Usage:
    //   <div class=help-me data-help-me-type=hover>Answer</div>
    //   <div id=help-me-answer class=help-me-msg>42</div>
    //type is 'hover' or 'load' and id is for styling and max displays
    loadWaitSeconds: 2,  //delay till start
    loadShowSeconds: 3,  //display duraton
    loadFadeSeconds: 2,  //duration of fade out
    loadMaxDisplays: 3,  //number of times to show per session
    loadDisplay: function() {
        var msgElem = $(this).find('.help-me-msg');
        var counter = msgElem.attr('id');
        if (library.counters.get(counter) < app.help.loadMaxDisplays) {
            msgElem
            .wrapInner('<div class=help-me-msg-inner><div class=help-me-msg-body>')
            .css('top', -msgElem.outerHeight(false))
            .delay(app.help.loadWaitSeconds * 1000)
            .fadeIn()
            .delay(app.help.loadShowSeconds * 1000)
            .fadeOut(app.help.loadFadeSeconds * 1000);
            library.counters.increment(counter);
        }
    },
    setup: function() {
        var elems = $('.help-me');
        elems.filter('[data-help-me-type=hover]').hoverIntent($.noop, $.noop);  //Not yet implemented
        elems.filter('[data-help-me-type=load]').each(app.help.loadDisplay);
    }
};


app.tetris = {
    best: function(slots) {
        var slot = slots.first();
        slots.each(function() {
            if ($(this).height() < slot.height()) slot = $(this);
        });
        return slot;
    },
    move: function(tetromino, slots) {
        app.tetris.best(slots).append(tetromino);
    },
    reset: function(matrix) {
        matrix.children().empty();
    },
    play: function(origin, matrix, options) {
        while (origin.children().length > 0)
            if (options && options.random) {
                var randomIndex = Math.floor(Math.random() * origin.children().length);
                this.move(origin.children().eq(randomIndex), matrix.children());
            }
            else {
                this.move(origin.children().first(), matrix.children());
            }
    }
};

app.facebook = {
    redirectUrl: null,
    updatePictures: function() {
        $('.facebook-picture').each(app.facebook.updatePicture);
    },
    updatePicture: function() {
        var elem = $(this);
        app.facebook.updatePictureElem(elem);
    },
    updatePictureElem: function(elem) {
        if (elem.exists() && !elem.find('img').exists()) {
            var id = elem.data('id');
            library.util.callMeMaybe('FB', function() {
                FB.api('/'+id+'/picture?type=large', function(response) {
                    var url = response.data ? response.data.url : '';
                    if (url && !elem.find('img').exists()) {
                        elem.append($('<img>').attr('src', url).attr('alt', 'Avatar'));
                    }
                });
            });
        }
    },
    setFacebookImage: function(elem, facebookId) {
        library.util.callMeMaybe('FB', function() {
            FB.api('/'+facebookId+'/picture?type=large', function(response) {
                var url = response.data ? response.data.url : '';
                if (url) {
                    elem.html('<img src='+url+'>');
                }
            });
        });
    },
    updateBkgnd: function(elem) {
        elem = elem || $(this);
        library.util.callMeMaybe('FB', function() {
            if (elem.data('id')) {
                var path = '/' + elem.data('id') + '/picture?type=large';
                FB.api(path, function(response) {
                    if (response.data && response.data.url)
                        elem.css({ 'background-image': 'url(' + response.data.url + ')' });
                });
            }
        });
    },
    updateCover: function() {
        var elem = $('#facebook-cover');
        if (elem.exists() && elem.css('background-image') == 'none') {
            var facebookId = $('#resource-fb-id').val();
            if (facebookId)
                library.util.callMeMaybe('FB', function() {
                    FB.api('/' + facebookId, function(response) {
                        if (response.cover && response.cover.source)
                            elem.css({ 'background-image': 'url(' + response.cover.source + ')' });
                    });
                });
        }
    },
    feedDialogCallback: function(response) {
        var postId = response.post_id;
        library.ui.refresh(app.facebook.redirectUrl);
    },
    feedDialog: function(title, caption, description, shareUrl, imageUrl, redirectUrl) {
        app.facebook.redirectUrl = redirectUrl;
        // calling the API ...
        var obj = {
                method: 'feed',
                redirect_uri: redirectUrl,
                link: shareUrl,
                picture: imageUrl,
                name: title,
                caption: caption,
                description: description
        };
        FB.ui(obj, app.facebook.feedDialogCallback);
    },
    sendDialogCallback: function(response) {
        // i don't think we have to do anything here
    },
    sendDialog: function(link, name, description, picture) {
        var obj = {
                method: 'send',
                link: link
        };
        // facebook doesn't seem to like it when you pass empty stuff
        if (name) {
            obj['name'] = name;
        }
        if (picture) {
            obj['picture'] = picture;
        }
        if (description) {
            obj['description'] = description;
        }
        library.util.callMeMaybe('FB', function() {
            FB.ui(obj, app.facebook.sendDialogCallback);
        });
    },
    shareTitle: 'Share this scaffold',
    shareDescription: "It's a very good scaffold",
    inviteFriends: function() {
        var url = $('#my-vanity-url').val();
        if (!url) {
            url = 'https://www.scaffold.com'
        }
        app.facebook.sendDialog(url, app.facebook.shareTitle,
                app.facebook.shareDescription);
    },
    shareFriends: function() {
        var url = $('#my-vanity-url').val();
        if (!url) {
            url = 'https://www.scaffold.com'
        }
        app.facebook.feedDialog(app.facebook.shareTitle, null, app.facebook.shareDescription,
                url, 'http://assets.scaffold.com/og-img.png',
                library.action.getBaseUri());
    },
    updatePage: function() {
        this.updatePictures();
        $('.facebook-bkgnd').each(this.updateBkgnd);
        this.updateCover();
    },
    setupActionTracking: function() {
        // code to track social interactions with FB
        library.util.callMeMaybe('FB', function() {
            FB.Event.subscribe('edge.create', function(targetUrl) {
                _gaq.push(['_trackSocial', 'facebook', 'like', targetUrl]);
            });
            FB.Event.subscribe('edge.remove', function(targetUrl) {
                _gaq.push(['_trackSocial', 'facebook', 'unlike', targetUrl]);
            });
            FB.Event.subscribe('message.send', function(targetUrl) {
                _gaq.push(['_trackSocial', 'facebook', 'send', targetUrl]);
            });
            FB.Event.subscribe('message.send', function(targetUrl) {
                _gaq.push(['_trackSocial', 'facebook', 'send', targetUrl]);
            });
            FB.Event.subscribe('comment.create', function(targetUrl) {
                _gaq.push(['_trackSocial', 'facebook', 'comment', targetUrl]);
            });
            FB.Event.subscribe('comment.remove', function(targetUrl) {
                _gaq.push(['_trackSocial', 'facebook', 'uncomment', targetUrl]);
            });
        });
    },
    setup: function() {
        library.ui.liveClick('.invite-friends', this.inviteFriends);
        if (library.action.isProduction()) {
            app.facebook.setupActionTracking();
        }
    }
};


app.testMode = {
    enabled: false,  //set to false for regular non-test mode
    key: 'test777',  //example usage: www.scaffold.com/?test777=true
    disable: '#to-disable',  //elements to hide in test mode
    setup: function() {
        if (this.enabled) {
            var dev = 'myapp.dev|localhost'.indexOf(window.location.hostname) !== -1;
            if (library.browser.getUrlVariables()[this.key] || dev)
                debug('Test mode: ' + window.location.hostname);
            else
                $('<style>' + this.disable + ' { display: none; } </style>').appendTo('head');
        }
    }
};

