// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import java.util.Date;

/**
 * Keeps track of what tokens (cookies) are associated
 *  to what user.
 * @author jwarner
 *
 */
class UserToken {
    String userToken
    Date created = new Date()

    static belongsTo = [user : User]

    static constraints = {
        userToken unique: true
    }

    static mapping = {
        datasources(['DEFAULT', 'readReplica'])
    }
}

