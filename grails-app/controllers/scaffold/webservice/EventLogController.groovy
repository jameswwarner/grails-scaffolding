// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold.webservice

import grails.converters.JSON
import scaffold.EventLoggingService


class EventLogController {
    EventLoggingService eventLoggingService

    static allowedMethods = [index: "POST"]

    def index() {
        def jsonPayload = request.JSON
        eventLoggingService.logJson(session.id, request.token, jsonPayload)
        render ([status: "ok"] as JSON)
    }

    def error() {
        log.error(params.msg)
        render ([status: "ok", msg: params.msg] as JSON)
    }

}
