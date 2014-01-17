// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

class RestUtil {
    static def dateFormat = "MM/dd/yyyy"

    static def setField(resource, params, field, converter) {
        if (params[field] == "")
            resource[field] = null
        else if (params[field] instanceof String)
            resource[StringUtil.toCamelCase(field)] = converter(params[field])
    }

    static def setFields(resource, params, fields) {
        //Example: setFields([id: 7, color: "tan"],
        //   [color: "red", score: "3"], ["color"])  ==>  [id: 7, color: "red" ]
        fields.each { setField(resource, params, it, { it }) }
    }

    static def setFieldsMap(resource, params, fields) {
        //Example: setFields([id: 7, color: "tan"],
        //   [option: "red"], [color: "option"])  ==>  [id: 7, color: "red" ]
        fields.each { setField(resource, params, it.key, { it.value }) }
    }

    static def setBooleanFields(resource, params, fields) {
        fields.each { setField(resource, params, it, { it.toBoolean() }) }
    }

    static def setIntegerFields(resource, params, fields) {
        fields.each { setField(resource, params, it,
            { it.isInteger() ? it.toInteger() : null }) }
    }

    static def setDateFields(resource, params, fields) {
        fields.each { setField(resource, params, it,
            { it.size() ? Date.parse(dateFormat, it) : null }) }
    }

    static def setLongFields(resource, params, fields) {
        fields.each { setField(resource, params, it,
            { it.isLong() ? it.toLong() : null }) }
    }

    static def setDollarFields(resource, params, fields) {  //"9.95" ==> 9.95
        fields.each { setField(resource, params, it,
            { it.isBigDecimal() ? it.toBigDecimal() * 100 : null }) }
    }

}
