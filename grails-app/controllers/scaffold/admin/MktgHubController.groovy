// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold.admin

import scaffold.StringUtil

public class MktgHubController {

    def index() {
    }

    def page() {
        render (template: StringUtil.toCamelCase(params.tab))
    }

}
