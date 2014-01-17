// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import scaffold.StringUtil


class SystemService implements ApplicationContextAware {

    def restPackageName = "scaffold.webservice"
    ApplicationContext applicationContext
    def grailsApplication

    Date buildTime() {
        return new Date(
            applicationContext.getResource("/WEB-INF").getFile().lastModified())
    }

    String pageUrl(request) {
        return "https://" + request.serverName + request.forwardURI
    }

    def getFile(filePath) {
        return applicationContext.getResource(filePath).getFile()
    }

    def getRestList() {
        def rest = [:]
        grailsApplication.controllerClasses.each {
            if (it.packageName == restPackageName)
                rest[StringUtil.toDash(it.name)] = it.getURIs().collect({ uri ->
                    StringUtil.toDash(it.getMethodActionName(uri))}).unique().sort() - "index"
            }
        return rest.sort()
    }

}
