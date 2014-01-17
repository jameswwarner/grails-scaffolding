//Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

if (!admin)
    var admin = {};

admin.cfg = {
    displayItem: function(data) {
        if (!data.error) {
            admin.cfg.setItem($('#site-cfg-' + data.id), data);
            library.action.statusMsg('Updated ' + data.code);
        }
    },
    updateItem: function(event) {
        var cfgItem = $(event.target).closest('tr');
        var id = cfgItem.attr('id').split('-')[2];  //id=site-cfg-7 --> 7
        var params = {
                value: cfgItem.find('.site-cfg-value input').val()
        };
        library.action.call('configuration', { action: 'update', id: id,
            params: params, callback: admin.cfg.displayItem });
    },
    setItem: function(cfgItem, cfgItemData) {
        cfgItem.attr('id', 'site-cfg-' + cfgItemData.id);
        cfgItem.find('.site-cfg-code').html(cfgItemData.code);
        cfgItem.find('.site-cfg-name').html(cfgItemData.name);
        cfgItem.find('.site-cfg-value input').val(cfgItemData.value);
    },
    insertItem: function(data) {
        if (!data.error) {
            var cfgItem = $('#site-cfg-row-stub').clone();
            admin.cfg.setItem(cfgItem, data);
            cfgItem.find('input').change(admin.cfg.updateItem);
            cfgItem.appendTo('#site-cfg tbody').show();
        }
    },
    display: function(data) {
        if (!data.error)
            data.forEach(function(itemData) {
                admin.cfg.insertItem(itemData);
            });
    },
    update: function() {
        library.action.call('configuration',
                { action: 'list-editable', callback: admin.cfg.display });
    }
};

admin.mktgHub = {
    tabRefresh: function() {
        var tabBase = 'mktg-hub-tab-';
        var tab = $(this).find('.ui-tabs-selected').attr('id');
        var module = library.util.toCamelCase(tab.replace(tabBase, ''));
        admin[module].update();
    },
    setup: function() {
        if ($('#page-hub-mktg').exists())
            $('#mktg-hub-tab-nav').tabs({ load: admin.mktgHub.tabRefresh });
    }
};
