//Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

'use strict';

//jQuery configuration
$.fx.speeds.xslow = 1000;   //1 second
$.fx.speeds.xxslow = 2000;  //2 seconds
$.fn.exists = function() { return this.length != 0; };  //has at least 1 element

if (!library) {
    var library = {};
}

library.util = {
    endsWith: function(str, suffix) {
        return str.indexOf(suffix, str.length - suffix.length) !== -1;
    },
    safeValue: function(value) {
        return value === undefined ? null : value;
    },
    contains: function(haystack, needle) {
        return haystack.indexOf(needle) != -1;
    },
    removeWhitespace: function(str) {
        return str.replace(/\s/g, '');
    },
    toCamelCase: function(dashStr) {  //ex: 'ready-set-go' ==> 'readySetGo'
        return dashStr.replace(/\-(.)/g, function() {
            return arguments[1].toUpperCase(); });
    },
    toDash: function(camelCaseStr) {  //ex: 'readySetGo' ==> 'ready-set-go'
        return camelCaseStr.replace(/([A-Z])/g, function() {
            return '-' + arguments[1].toLowerCase(); });
    },
    lookDownThenUp: function(elemStart, selector) {
        var elem = elemStart.find(selector);
        return elem.exists() ? elem : elemStart.closest(selector);
    },
    lookUpThenDown: function(elemStart, selector) {
        var elem = elemStart.closest(selector);
        return elem.exists() ? elem : elemStart.find(selector);
    },
    copyTemplate: function(templateId, holderElem) {
        //Usage:
        //   1) <div><p id=color-template><p>red</p></div>
        //   2) library.util.copyTemplate('color-template').text('aqua');
        //   3) <div><p id=color-template><p>red</p><p>aqua</p></div>
        var template = $('#' + templateId);
        if (!holderElem)
            holderElem = template.parent();
        return template.clone(true, true).removeAttr('id').appendTo(holderElem);
    },
    details: function(x) {
        var msg = typeof x + ' --> ';
        if (x && x.jquery)
            msg = msg + 'jquery:' + x.jquery + ' elems:' + x.length +
            (x.length == 0 ? '' : ' [#1' +
                    ' elem:' +  x.first()[0].nodeName +
                    ' id:' +    x.first().attr('id') +
                    ' class:' + x.first().attr('class') +
                    ' kids:' +  x.first().children().length + ']');
        else if (x == null)
            msg = msg + '[null]';
        else if (typeof x == 'object')
            for (var property in x)
                msg = msg + property + ':' + x[property] + ' ';
        else
            msg = msg + x;
        return msg;
    },
    msg: function(msg) {
        $.get(library.action.getBaseUri() + 'rest/event-log/error?msg=' +
                encodeURIComponent(msg));
    },
    callMeMaybe: function(objName, funcToCall) {
        //Calls function (funcToCall) after object (named objName) exists
        if (window[objName])
            funcToCall();
        else
            window.setTimeout(function() { library.util.callMeMaybe(objName,
                    funcToCall); }, 100);
    },
    stopPropagation: function(event) {
        if (event.stopPropagation)  //Modern browsers
            event.stopPropagation();
        else                        //IE
            window.event.cancelBubble = true;
    }
};

library.db = {
    save: function(key, obj) {
        return localStorage[key] = JSON.stringify(obj);
    },
    read: function(key) {
        return JSON.parse(library.util.safeValue(localStorage[key]));
    }
};

library.session = {
    save: function(key, value) {
        return sessionStorage[key] = JSON.stringify(value);
    },
    read: function(key) {
        return JSON.parse(library.util.safeValue(sessionStorage[key]));
    }
};

library.counters = {
    store: 'counters',
    list: function() {
        var c = sessionStorage[library.counters.store];
        return c ? JSON.parse(c) : {};
    },
    get: function(name) {
        var c = library.counters.list();
        return c[name] ? c[name] : 0;
    },
    set: function(name, count) {
        var c = library.counters.list();
        c[name] = count;
        sessionStorage[library.counters.store] = JSON.stringify(c);
        return c[name];
    },
    reset: function(name) {
        return library.counters.set(name, 0);
    },
    increment: function(name) {
        return library.counters.set(name, library.counters.get(name)+1);
    }
};

library.ui = {
    safe: function(string) {
        return string ? string : '';
    },
    refresh: function(url) {
        if (url)
            window.location = url;
        else
            window.location.reload(true);
    },
    popup: function(url, options) {
        var settings = { width: 500, height: 300 };
        $.extend(settings, options);
        window.open(url, '_blank', 'width=' + settings.width + ',height=' +
                settings.height + ',left=200,location=no,scrollbars=yes,resizable=yes');
    },
    setDisable: function(event) {
        $(event.target).attr('disabled', true);
    },
    clearDisable: function() {
        $('.disable-on-click').attr('disabled', false);
    },
    liveClick: function(selector, callback) {
        $(document).on('click', selector, callback);
    },
    displayAddr: function() {
        //Usage:
        //   <p class=display-addr data-name=sales data-level2=example data-code=c></p>
        $('.display-addr').each(function() { $(this).html($(this).data('name') +
                '<span>' + String.fromCharCode(57+7) + $(this).data('level2') +
                '.</span>' + { c:'com', o:'org', n:'net' }[$(this).data('code')]); });
    },
    jumpClick: function(event) {
        //Usage:
        //   <button class=jump-click data-url="/" data-new=true>Home</button>
        var elem = $(event.target).closest('.jump-click');
        var url = elem.data('url');
        if (elem.data('new') && url)
            window.open(url, "_blank");
        else if (url)
            window.location = url;
    },
    popupClick: function(event) {
        //Usage (see popup() for default width and height):
        //   <button class=popup-click data-url="/" data-width=300>Home</button>
        var elem = $(event.target);
        var options = { width: elem.data('width'),  height: elem.data('height') };
        if (elem.data('url'))
            library.ui.popup(elem.data('url'), options);
    },
    autoFocus: function(elem) {
        var types = '[type=text],[type=email],[type=password]';
        var firstField = elem.find('input').filter(types).first();
        return firstField.not('.no-auto-focus').focus();
    },
    setup: function() {
        $('a.external-site').attr('target', '_blank');
        $('a img').parent().addClass('plain');
        $('.disable-on-click').click(this.setDisable);
        library.ui.liveClick('.jump-click', library.ui.jumpClick);
        library.ui.liveClick('.popup-click', library.ui.popupClick);
        this.displayAddr();
    }
};

library.dropDown = {
    //Usage:
    //   <ul>
    //      <li>Not a drop down</li>
    //      <li class=drop-down>Hover over me<li>
    //         <ul>
    //            <li>It's my turn</li>
    //            <li>In and out</li>
    //         </ul>
    //   </ul>
    hideId: undefined,
    getElem: function(event) {
        return library.util.lookUpThenDown($(event.target), 'li.drop-down').find('ul.drop-down-choices');
    },
    showMenu: function(event) {
        if (library.dropDown.hideId) {
            window.clearTimeout(library.dropDown.hideId);
            library.dropDown.hideId = null;
        }
        var elem = library.dropDown.getElem(event);
        elem.slideDown()
        .hoverIntent($.noop, library.dropDown.hideMenu);
    },
    hideMenu: function(event) {
        if (!library.dropDown.hideId) {
            // on hide if there is not already a hide pending
            library.dropDown.hideId = window.setTimeout(
                    function() {
                        $('li.drop-down ul.drop-down-choices').slideUp();
                        library.dropDown.hideId = null;
                    }, 750);
        }
    },
    setup: function() {
        $('li.drop-down').hoverIntent(
                library.dropDown.showMenu,
                library.dropDown.hideMenu);
        library.ui.liveClick('li.drop-down', library.dropDown.showMenu);
    }
};

library.infoRollover = {
    //Usage:
    //   <div class=info-rollover-area>
    //      Main content
    //      <div class=info-rollover-msg>Flyover content</div>
    //   </div>
    getElem: function(event) {
        return $(event.target).closest('.info-rollover-area')
        .find('.info-rollover-msg');
    },
    showMsg: function(event) {
        library.infoRollover.getElem(event).fadeIn();
    },
    hideMsg: function(event) {
        library.infoRollover.getElem(event).fadeOut();
    },
    setup: function() {
        $('.info-rollover-area').hoverIntent({
            over:        library.infoRollover.showMsg,
            out:         library.infoRollover.hideMsg,
            sensitivity: 30
        });
    }
};

library.expandable = {
    toggle: function(event) {
        var elem = $(event.target).parent();
        if (elem.hasClass('expandable-box')) {
            elem.removeClass('expandable-box').addClass('expandable-box-open').height('auto');
        }
        else {
            elem.removeClass('expandable-box-open').addClass('expandable-box').height(elem.data('expandable-height'));
        }
    },
    add: function(elems, options) {
        //Options:
        //   height - y pixels of element when collapsed (default 200)
        var settings = { height: 200 };
        $.extend(settings, options);
        elems.filter(function() { return ($(this).height() > settings.height && !$(this).find('.expandable-toggle').exists()); })
        .height(settings.height)
        .data('expandable-height', settings.height)
        .addClass('expandable-box')
        .append('<div class=expandable-toggle>');
    },
    setup: function() {
        library.ui.liveClick('.expandable-toggle', library.expandable.toggle);
    }
};

library.imageSelector = {
    //Usage:
    //   <div id=image-selector-destination></div>
    //   ...
    //   <div class=image-selector-preview><img src=cat.png></div>
    //   <div class=image-selector-preview><img src=dog.png></div>
    selectedClass: 'image-selected',
    select: function(event) {
        var elem = $(event.target).closest('.image-selector-preview');
        if (!elem.hasClass(library.imageSelector.selectedClass)) {
            $('.image-selector-preview')
            .removeClass(library.imageSelector.selectedClass);
            elem.addClass(library.imageSelector.selectedClass);
            library.imageSelector.dest.find('img')
            .hide()
            .attr('src', elem.find('img').attr('src'))
            .fadeIn();
        }
    },
    setup: function() {
        library.imageSelector.dest = $('#image-selector-destination');
        if (library.imageSelector.dest.exists()) {
            library.imageSelector.dest.append('<img>');
            $('.image-selector-preview')
            .hoverIntent(library.imageSelector.select, $.noop)
            .click(library.imageSelector.select)
            .first().click();
        }
    }
};

library.animate = {
    fadeInMsg: function(elem, msg) {
        //Set elem's content to the msg and fade it in
        return elem.html(msg).hide().fadeIn();
    },
    rollIn: function(elems, options) {
        var settings = { startDelay: 'slow', fadeDelay: 'fast' };
        $.extend(settings, options);
        if (elems.eq(0).css('opacity') == 1) {
            elems.fadeTo(0, 0);
            $(window).load(function() { library.animate.rollIn(elems, settings); });
        }
        else {
            elems.eq(0).delay(settings.startDelay).fadeTo(settings.fadeDelay, 1,
                    function() { (elems = elems.slice(1)).length && library.animate.rollIn(
                            elems, { startDelay: 0, fadeDelay: settings.fadeDelay }) });
        }
    },
    fadeToVisible: function(elems, options) {
        var settings = { delay: 0, speed: 'slow', msg: null };
        $.extend(settings, options);
        elems.stop(true, true).css('opacity', 0);
        if (settings.msg)
            elems.html(settings.msg);
        return elems.delay(settings.delay).fadeTo(settings.speed, 1);
    }
};

library.images = {
    activateElem: function() {
        var elem = $(this).empty();
        elem.append($('<img>').attr('src', elem.data('url')));
        if (elem.data('display'))
            elem.hide().fadeIn();
    },
    activate: function(options) {
        //Usage:
        //   <div class=image-activate data-url=wow.jpg></div>
        //   --> <div class=image-activate data-url=wow.jpg><img src=wow.jpg></div>
        var settings = { selector: '.image-activate', display: true };
        $.extend(settings, options);
        $(settings.selector).data('display', settings.display).each(this.activateElem);
    }
};

library.tables = {
    displayRows: function(tableElem, data, options) {
        //Column headers come from data-col-name attr on th unless "header" option is supplied
        var settings = { header: [], fields: [] };
        $.extend(settings, options);
        if (settings.header.length == 0)  //get header from th attr: <th data-col-name=my-code>My Code</th>
            $.each(tableElem.find('thead th'), function() {
                settings.header.push(library.util.toCamelCase($(this).data('col-name')));
            });
        var rowText = '';
        for (var row in data) {
            var rowData = '';
            for (var count in settings.fields)
                rowData = rowData + ' data-' + settings.fields[count] + '=' +
                data[row][settings.fields[count]];
            rowText = rowText + '<tr' + rowData + '>';
            for (var field in settings.header) {
                var content = data[row][settings.header[field]];
                if (content == null)
                    content = '';
                else if (settings.header[field].indexOf('Date') != -1)
                    content = $.format.date(
                            data[row][settings.header[field]], 'MM/dd/yyyy');
                rowText = rowText + '<td>' + content + '</td>';
            }
            rowText = rowText + '</tr>';
        }
        tableElem.find('tbody').html(rowText);
    }
};

library.browser = {
        getUrlVariables: function() {
            //Example:
            //   http://example.com?lang=jp&code=7  ==>  { lang: 'jp', code: 7 }
            var vars = {};
            var pairs = location.search.substring(1).split('&');
            for (var count in pairs) {
                var pair = pairs[count].split('=');
                vars[pair[0]] = pair[1];
            }
            return vars;
        }
};

library.bubbleHelp = {
    //Usage:
    //   <div>Hover over me<div class=bubble-help>Help!</div></div>
    elem: null,
    hi: function(event) {
        var hoverElem = $(event.target).closest('.bubble-help-hover');
        library.bubbleHelp.elem = hoverElem.find('.bubble-wrap');
        if (library.bubbleHelp.elem.length == 0)
            library.bubbleHelp.elem = hoverElem.find('.bubble-help')
            .wrap('<div class=bubble-wrap></div>')
            .parent().append('<div>&#9660;</div>');
        library.bubbleHelp.elem.find('.bubble-help').show();
        library.bubbleHelp.elem.css({ top: -library.bubbleHelp.elem.height() })
        .hide().fadeIn();
    },
    bye: function() {
        library.bubbleHelp.elem.stop(true).fadeOut('slow');
    },
    setup: function() {
        $('.bubble-help').parent().addClass('bubble-help-hover')
        .hover(this.hi, this.bye);
    }
};

library.action = {
    eventBlock: false,
    setEventBlock: function() { library.action.eventBlock = true; },
    clearEventBlock: function() { library.action.eventBlock = false; },
    baseUri: null,
    getBaseUri: function() {
        if (!library.action.baseUri)
            library.action.baseUri = $('#base-uri').val();
        return library.action.baseUri;
    },
    isLoggedIn: function() {
        if ($('#my-vanity-url').val()) {
            return true;
        }
        return false;
    },
    isProduction: function() {
        if ($('#env-production').exists()) {
            return true;
        }
        return false;
    },
    statusMsg: function(msg) {
        var delayVerySlow = 8000;
        if ($('#status-msg p').length == 0)
            $('#status-msg').append('<p></p>');
        $('#status-msg p')
        .html(msg)
        .hide()
        .stop()
        .fadeIn('slow')
        .fadeOut(delayVerySlow);
    },
    safeFocus: function(elem) {
        if (elem != undefined) {
            library.action.setEventBlock();
            elem.focus();
            window.setTimeout(library.action.clearEventBlock, 1000);
        }
    },
    onReturnKey: function(elems, callback) {
        elems.keyup(function(event) {
            if (event.keyCode == 13 && !library.action.eventBlock)
                callback();
        });
    },
    call: function(resource, options) {
        var settings = {
                action:          null,
                id:              null,
                params:          null,
                displayStatus:   true,
                callback:        null,
                callbackOptions: null
        };
        $.extend(settings, options);
        var url = library.action.getBaseUri() + 'rest/' + resource +
        (settings.action ? '/' + settings.action : '') +
        (settings.id ? '/' + settings.id : '');
        for (var key in settings.params)
            if (settings.params[key] != undefined)
                url = url + '&' + key + '=' + encodeURIComponent(settings.params[key]);
        url = url.replace(/&/, '?');
        //debug(url.replace(/password.*/, '*****'));
        $.getJSON(url, function(data) {
            if (settings.displayStatus && data.error) {
                library.action.statusMsg(data.msg);
            }
            if (data.error && data.code == 104) { // not logged in error
                app.login.showLoginFlyover();
            }
            if (settings.callback)
                settings.callback(data, settings.callbackOptions);
        });
    }
};

library.dialog = {
    //Usage:
    //   <button id=action-{NAME} class=dialog-action>  (displays the dialog)
    //   ...
    //   <div id=dialog-{NAME} class=dialog title="Ok">  (the dialog)
    //Alternative auto open usage without button (with optional width):
    //   <div id=dialog-auto-open data-dialog-width=500 class=dialog>
    //Alternative simple js alert:
    //   library.dialog.alert('Hi');
    //TODO: convert id to data to support multiple links per page (see: id=action-why-join)
    alert: function(msg, options) {
        var settings = { title: 'Message', button: 'Ok' };
        $.extend(settings, options);
        var elem = $('#dialog-alert');
        if (!elem.exists()) {
            var buttonElem = $('<button type=button class=cancel>')
                .text(settings.button)
                .click(function() {
                    $(this).closest('.dialog').dialog('close');
                });
            elem = $('<div id=dialog-alert class=dialog><p id=alert-msg></p></div>')
                .attr('title', settings.title)
                .append(buttonElem)
                .appendTo($('body'))
                .dialog({ modal: true });
        }
        elem.dialog('open').find('#alert-msg').html(msg);
    },
    errMsg: function(dialogElem, msg) {
        return library.animate.fadeInMsg(dialogElem.find('.dialog-error-msg'), msg);
    },
    cleanup: function(event) {
        var dialogElem = $(event.target).closest('.ui-dialog').find('.dialog');
        library.action.safeFocus($('#' + dialogElem.data('close-focus-id')));
    },
    initialize: function(dialogElem) {
        var dialogOptions = {
                modal: true,
                autoOpen: false,
                width: dialogElem.data('dialog-width'),
                close: library.dialog.cleanup
        };
        dialogElem.dialog(dialogOptions);
        dialogElem.find('p input:text').not('[class^="field"]').addClass('field-small');
        var buttonElem = dialogElem.find('button');
        if (buttonElem.exists())
            buttonElem.wrapAll('<p></p>').parent().append('<button type=button class=cancel>Cancel</button>');
        else
            dialogElem.append('<p><button type=button class=cancel>Ok</button></p>');
        dialogElem.find('.cancel').click(function() {
            $(this).closest('.dialog').dialog('close');
        });
        if (!dialogElem.find('.dialog-error-msg').exists())
            dialogElem.append('<p class=dialog-error-msg></p>');
        dialogElem.find('.dialog-error-msg').hide();
    },
    show: function(dialogElem) {
        if (!dialogElem.hasClass('ui-dialog-content'))
            library.dialog.initialize(dialogElem);
        dialogElem.dialog('open');
        library.ui.autoFocus(dialogElem);
        return dialogElem;
    },
    hide: function(dialogElem) {
        if (dialogElem != undefined) {  //TODO: investigate... should method ever be passed undefined?  hmmm
            dialogElem.dialog('close');
            library.action.safeFocus($('#' + dialogElem.data('close-focus-id')));
        }
    },
    hi: function(event) {
        var actionId = $(event.target).closest('.dialog-action').attr('id');
        library.dialog.show($('#' + actionId.replace('action', 'dialog')));
    },
    setup: function() {
        library.ui.liveClick('.dialog-action', this.hi);
        if ($('#dialog-auto-display').exists())
            library.dialog.show($('#dialog-auto-display'));
        $('.ui-datepicker').hide();  //Workaround to fix datepicker ghost div
    }
};

library.loading = {
    on: function(mask) {
        var loadingSrc = $('#loading-image').attr('src');
        $.loading(true, { mask: mask, align: 'top-center', img: loadingSrc });
    },
    off: function() {
        $.loading(false);
    }
};

library.validate = {
    emailPattern:   /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]+$/,
    zipPattern:     /^[0-9]{5}(-[0-9]{4})?$/,
    orderNoPattern: /^J[0-9]+$/,
    isEmailValid: function(email) {
        return email.match(library.validate.emailPattern);
    },
    isZipValid: function(zip) {
        return zip.match(library.validate.zipPattern);
    },
    isOrderNoValid: function(orderNo) {
        return orderNo.match(library.validate.orderNoPattern);
    }
};

library.elems = {
    nextWrap: function(elem) {
        return elem.next().exists() ? elem.next() : elem.siblings().first();
    }
};

library.json = {
    replacer: function(match, pIndent, pKey, pVal, pEnd) {
        var key = '<span class=json-key>';
        var val = '<span class=json-value>';
        var str = '<span class=json-string>';
        var r = pIndent || '';
        if (pKey)
            r = r + key + pKey.replace(/[": ]/g, '') + '</span>: ';
        if (pVal)
            r = r + (pVal[0] == '"' ? str : val) + pVal + '</span>';
        return r + (pEnd || '');
    },
    prettyPrint: function(obj) {
        var jsonLine = /^( *)("[\w]+": )?("[^"]*"|[\w.+-]*)?([,[{])?$/mg;
        return JSON.stringify(obj, null, 3)
        .replace(/&/g, '&amp;').replace(/\\"/g, '&quot;')
        .replace(/</g, '&lt;').replace(/>/g, '&gt;')
        .replace(jsonLine, library.json.replacer);
    }
};

library.fixIE = {
    minSupportedIE: 8,
    setup: function() {
        if (msie && msie < this.minSupportedIE)
            $('#screen-dim, #unsupported-browser').show();
        if (msie && msie <= 9) {
            $('input').placeholder();
            if (!Date.now)
                Date.now = function() { return new Date().valueOf(); };
                $.support.cors = true;  //try this to fix the IE ajax issues
        }
    }
};

//Print text or object message to output
function debug(thing) { library.util.msg(new Date().getTime() + ': ' +
        library.util.details(thing)); }
