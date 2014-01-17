// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold.webservice

import scaffold.Configuration
import grails.converters.JSON

import scaffold.RestUtil
import scaffold.StringUtil
import scaffold.WebserviceError


class ConfigurationController {

    def index() {  //example: rest/configuration?code=access-key
        def resource = params.code ? Configuration.findByCode(params.code) :
            Configuration.get(params.id)
        if (!resource)
            resource = WebserviceError.resourceNotFound
        render resource as JSON
    }

    def add() {  //example: rest/configuration/add
        def resource = new Configuration()
        resource.save()
        render resource as JSON
    }

    def update() {  //example: rest/configuration/update/1?value=hello
        def resource = Configuration.get(params.id)
        if (!resource) {
            resource = WebserviceError.resourceNotFound
        }
        else {
            RestUtil.setFields(resource, params, ["code", "name", "value"])
            RestUtil.setBooleanFields(resource, params, ["editable"])
            resource.code = StringUtil.toDash(resource.code);
            resource.save()
        }
        render resource as JSON
    }

    def list() {  //example: rest/configuration/list
        def resource = Configuration.list(sort: "code")
        render resource as JSON
    }

    def listEditable() {  //example: rest/configuration/list-editable
        def resource = Configuration.findAllByEditable(true, [sort: "code"])
        render resource as JSON
    }

}
