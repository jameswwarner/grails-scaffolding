// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

modules = {

    core {
        dependsOn "jquery-ui"
        dependsOn "handlebars_runtime"
        resource url: "/js/plugins/jquery.dateFormat-1.0.js"  //https://github.com/phstc/jquery-dateFormat
        resource url: "/js/plugins/jquery.hoverIntent.js"
        resource url: "/js/plugins/jquery.loading-1.6.1.js"  //https://jquery-values.googlecode.com/svn/other/loading/
        resource url: "/js/plugins/jquery.placeholder.js"
        resource url: "/js/plugins/jquery.scrollExtend.min.js"
        resource url: "/js/plugins/jquery.safeEnter.min.js"
        resource url: "/js/plugins/jquery.easing.1.1.2.js"
        resource url: "/js/plugins/jquery.ui.totop-1.2.min.js"
        resource url: "/js/plugins/buckets-minified.js"
        resource url: "/js/plugins/dna.js"
        resource url: "/js/library.js"
        //resource url: "/images/**"  //TODO: figure out way to include all images
    }

    site {
        dependsOn "core"
        resource url: "/js/social.js"
        resource url: "/css/key/themes/stark/jquery-ui-1.10.1.custom.css"
        resource url: "/css/key/site.less", attrs: [rel: "stylesheet/less", type: "css"]
        resource url: "/js/plugins/jquery.jcarousel.js"
        resource url: "/js/plugins/jquery.history-1.7.1.js"  //https://github.com/browserstate/history.js/blob/master/scripts/bundled/html4%2Bhtml5/jquery.history.js
        resource url: "/js/app.js"
        resource url: "/js/login.js"
        resource url: "/js/gateway.js"
        resource url: "/js/user-settings.js"
        resource url: "/js/start.js"
    }

    admin {
        dependsOn "core"
        resource url: "/css/key/themes/smoothness/jquery-ui-1.10.1.custom.css"
        resource url: "/css/key/admin.less", attrs: [rel: "stylesheet/less", type: "css"]
        resource url: "/js/plugins/jquery.doubleSuggest.js"
        resource url: "/js/admin/img-library.js"
        resource url: "/js/admin/marketing.js"
        resource url: "/js/admin/operations.js"
        resource url: "/js/admin/start.js"
    }

}
