// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import java.util.Date;

/**
 * Records visits to the site by a given user
 * @author jwarner
 *
 */
class UserVisit {
    String userToken
    String referer
    String userAgent
    Date created = new Date()

    static belongsTo = [user : User]

    static constraints = {
        userToken nullable: true
        referer nullable: true
        userAgent nullable: true
    }

    static mapping = {
        datasources(['DEFAULT', 'readReplica'])
    }
}
