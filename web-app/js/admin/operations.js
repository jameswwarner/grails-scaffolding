// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

if (!admin)
   var admin = {};

admin.siteCfg = {
    deleteItem: function() {
        library.action.statusMsg('Why did you create me in the first place?');
    },
    displayItem: function(data) {
        if (!data.error) {
            admin.siteCfg.setItem($('#site-cfg-' + data.id), data);
            library.action.statusMsg('Updated: ' + data.code);
        }
    },
    updateItem: function(event) {
      var cfgItem = $(event.target).closest('tr');
      var id = cfgItem.attr('id').split('-')[2];  //id=site-cfg-7 ==> 7
      var params = {
         code:     cfgItem.find('.site-cfg-code input').val(),
         name:     cfgItem.find('.site-cfg-name input').val(),
         value:    cfgItem.find('.site-cfg-value input').val(),
         editable: cfgItem.find('.site-cfg-editable input').is(':checked')
         };
      library.action.call('configuration', { action: 'update', id: id,
         params: params, callback: admin.siteCfg.displayItem });
    },
    setItem: function(cfgItem, cfgItemData) {
        cfgItem.attr('id', 'site-cfg-' + cfgItemData.id);
        cfgItem.find('.site-cfg-id').text(cfgItemData.id);
        cfgItem.find('.site-cfg-code input').val(cfgItemData.code);
        cfgItem.find('.site-cfg-name input').val(cfgItemData.name);
        cfgItem.find('.site-cfg-value input').val(cfgItemData.value);
        cfgItem.find('.site-cfg-editable input').attr('checked', cfgItemData.editable);
    },
    insertItem: function(data) {
        if (!data.error) {
            var cfgItem = $('#site-cfg-row-stub').clone();
            admin.siteCfg.setItem(cfgItem, data);
            cfgItem.find('input').change(admin.siteCfg.updateItem);
            cfgItem.find('.site-cfg-action-delete').click(admin.siteCfg.deleteItem);
            cfgItem.appendTo('#site-cfg tbody').fadeIn();
        }
    },
    display: function(data) {
        if (!data.error)
            data.forEach(function(itemData) {
            admin.siteCfg.insertItem(itemData);
        });
    },
    addItem: function() {
        library.action.call('configuration',
            { action: 'add', callback: admin.siteCfg.insertItem });
    },
    update: function() {
        library.action.call('configuration',
            { action: 'list', callback: admin.siteCfg.display });
        $('#site-cfg-action-add').click(admin.siteCfg.addItem);
    }
};

admin.statusInfo = {
    updateRestResult: function(elem, url) {
        var delimJammed = /,"/g;
        var delimSpaced = ',\n"';
        $.getJSON(url, function(data) {
            elem.text(JSON.stringify(data).replace(delimJammed, delimSpaced));
        });
    },
    newElem: function(template) {
        return library.util.copyTemplate(template).removeClass('hide-me');
    },
    update: function() {
        var restTests = [
            { resource: 'configuration',  uris: ['/1', '/list'] },
            { resource: 'category',       uris: ['/3'] },
            { resource: 'bogus',          uris: ['/'] }
        ];
        for (var t in restTests) {
            var resource = restTests[t].resource;
            admin.statusInfo.newElem('test-header-template').text(resource);
            for (var u in restTests[t].uris) {
                var uri = restTests[t].uris[u];
                var url = library.action.getBaseUri() + 'rest/' + resource + uri;
                admin.statusInfo.newElem('test-url-template').find('a')
                   .attr('href', url)
                   .attr('target', '_blank')
                   .text(url);
                var resultElem = admin.statusInfo.newElem('test-result-template');
                admin.statusInfo.updateRestResult(resultElem.find('code'), url);
            }
        }
    }
};

admin.userMgmt = {
    display: function(data) {
        var usersHeader = ['id', 'email', 'lastName', 'firstName', 'created'];
        var tableElem = $('<table class=data-box><thead><tr><th>ID</th><th>Email</th><th>Last Name</th><th>First Name</th><th>Created</th></tr></thead><tbody></tbody></table>');
        tableElem.appendTo($('#user-mgmt'));
        library.tables.displayRows(tableElem, data, { header: usersHeader });
    },
    update: function() {
        //library.action.call('user', { action : 'list', callback: admin.userMgmt.display });
    }
};

admin.restTool = {
    urlElem: null,
    response: function(data) {
        var dataObj = { url: admin.restTool.urlElem.text(),
            json: library.json.prettyPrint(data) };
        dna.clone('rest-response', dataObj, { fade: true, top: true });
    },
    request: function() {
        debug(admin.restTool.urlElem.text().replace(/password.*/, '*****'));
        $.getJSON(admin.restTool.urlElem.text(), admin.restTool.response);
    },
    refresh: function() {
        var resource = $('input:radio[name=rest-resource]:checked').val();
        var action = $('input:radio[name=rest-action-' + resource + ']:checked').val();
        var id = $('#rest-id').val();
        var params = $('#rest-params').val();
        var actionsId = $('.rest-actions .rest-options:visible').attr('id');
        if (resource && 'rest-actions-' + resource != actionsId) {
            $('.rest-actions .rest-options').hide();
            $('#rest-url, #rest-submit, .rest-actions, #rest-actions-' + resource).fadeIn();
        }
        admin.restTool.urlElem.text(library.action.getBaseUri() + 'rest/' +
             resource +
             (action && action != 'index' ? '/' + action : '') +
             (id ? '/' + id : '') +
             (params ? '?' + params : ''));
    },
    update: function() {
        this.urlElem = $('#rest-url');
        $('.rest-options input, #rest-id').change(admin.restTool.refresh);
        $('.rest-extras input').keyup(admin.restTool.refresh);
        $('#rest-submit').click(admin.restTool.request);
    }
};

admin.opsHub = {
    tabRefresh: function() {
        var tabBase = 'ops-hub-tab-';
        var tab = $(this).find('.ui-tabs-selected').attr('id');
        var module = library.util.toCamelCase(tab.replace(tabBase, ''));
        admin[module].update();
    },
    setup: function() {
        if ($('#page-hub-ops').exists()) {
            $('#ops-hub-tab-nav').tabs({ load: admin.opsHub.tabRefresh });
        }
    }
};
