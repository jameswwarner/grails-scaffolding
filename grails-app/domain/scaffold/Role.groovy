// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

/**
 * Security roles for users
 * @author jwarner
 */
enum Role {
    admin // people with admin access: future, make this more granular

    static mapping = {
        datasources(['DEFAULT', 'readReplica'])
    }
}

