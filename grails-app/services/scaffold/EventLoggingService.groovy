// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import grails.converters.JSON
import org.apache.log4j.Logger


public class Event {
    String sessionId
    String cookieId
    Date date
    Object payload
}

class EventLoggingService {
    def grailsApplication
    def eventLog = Logger.getLogger("eventLog")

    def log(Event event) {
        eventLog.info(event as JSON)
    }

    def logJson(def sessionId, def cookieId, def jsonVal) {
        def event = new Event(sessionId: sessionId, cookieId: cookieId,
            date: new Date(), payload: jsonVal)
        log(event)
    }

    def logView(def sessionId, def cookieId, def url) {
        def event = new Event(sessionId: sessionId, cookieId: cookieId,
            date:new Date(), payload: url)
        log(event)
    }

    String getLogDirName() {
        return grailsApplication.config.logHome
    }

    String getLogBackupDirName() {
        return grailsApplication.config.logBackupFolder
    }

}
