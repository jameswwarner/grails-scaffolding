// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold.admin


import scaffold.StringUtil


public class OpsHubController {

    def systemService

    def index() {
    }

    def page() {
        def model = [:]
        if (params.tab == "status-info")
            model.buildTime = systemService.buildTime()
        if (params.tab == "rest-tool")
            model.restList = systemService.getRestList()
        render (template: StringUtil.toCamelCase(params.tab), model: model)
    }

}
