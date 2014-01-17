//Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

if (!admin)
    var admin = {};

admin.imageLibrary = {
    update: function() {
        initializeImages();
        setupAllImageAutosave();
        setupImageSearch();
    }
};

function editImageJson(id, data) {
    if (data.error)
        library.dialog.errMsg(dialogElem, data.msg);
    else {
        // add the image tag
        if ($('#current-image-pane div').length == 0)
            $('#current-image-pane').append('<div></div>');
        if (imageId != id) {
            imageId = id; // set global current image  //TODO: Move variable into a module -- no globals
            $('#current-image-pane div').empty();
            $('#current-image-pane div').append('<img class=image-thumb src="//'+data.url+'">');
        }
        $('#image-name').val(data.name);
        setupTags(data.tags);
    };
}

function editImage(id) {
    // this will get the form  from the server not the page to prevent
    //  dirty writes
    var url = library.action.getBaseUri() + 'rest/image/' + id;
    $.getJSON(url, function(data) {
        editImageJson(id, data);
    });
}

function setupImageAutosave(element, paramName) {
    $(element).blur(function() {
        var newVal = $(element).val();
        url = library.action.getBaseUri() + 'rest/image/update/' + imageId +
        '?'+paramName+'=' + encodeURI(newVal);
        $.getJSON(url, function(data) {
            library.action.statusMsg(data.error ? data.msg : 'Image saved');
            editImageJson(imageId, data);
            initializeImages();
        });
    });
}

function setupAllImageAutosave() {
    setupImageAutosave('#image-name', 'name');
}

function initializeImages() {
    var elem = $('#image-list');
    if (elem.find('tbody').length == 0) {
        return;
    }
    // this should use the offer id to identify the current offer
    var url = library.action.getBaseUri() + 'rest/image/list';
    $.getJSON(url, function(data) {
        if (data.error)
            library.dialog.errMsg(dialogElem, data.msg);
        else {
            elem.find('tbody').empty();
            $.each(data, function(i, image) {
                elem
                .append($('<tr>')
                        .click(function() {
                            editImage(image.id);
                        })
                        .append($('<td>')
                                .html(image.name)
                        )
                        .append($('<td>')
                                .html(image.path)
                        )
                        .append($('<td>')
                                .html(image.createdDate ? $.format.date(image.createdDate, "MM/dd/yyyy") : '')
                        )
                );
            });
        };
    });
}

function setupImageSearch() {
    $('#image-search').doubleSuggest({
        localSource: null,
        remoteSource: library.action.getBaseUri() + 'rest/image/search',
        selectValue: 'name',
        seekValue: 'name', // if we have seed data and only want to search some fields
        queryParam: 'query',
        onSelect: function(data, $elem) {
            editImage(data.id);
        }
    });
}
