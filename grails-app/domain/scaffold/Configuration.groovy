// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

// Site configuration settings for editable application properties
class Configuration {
    String code       //key to lookup cfg item (ex: "access-secret")
    String name       //description of cfg item (ex: "Access code for tour")
    String value      //data for cfg item (ex: "obsessed-vip")
    Boolean editable  //cfg item value can edited in admin UI?

    static mapping = {
        value sqlType: 'varchar(1024)'
        datasources(['DEFAULT', 'readReplica'])
    }

    static constraints = {
        code nullable: true
        name nullable: true
        value nullable: true, maxSize: 1024
        editable nullable: true
    }

    public static String lookup(code) {
        def cfg = Configuration.findByCode(code)
        if (!cfg)
            org.apache.log4j.Logger.getLogger(this).error("Lookup failed: " + code)
        return cfg?.value
    }

    public static Boolean featureLookup(name) {
       return lookup("features")?.tokenize(',')?.contains(name)
    }

}
