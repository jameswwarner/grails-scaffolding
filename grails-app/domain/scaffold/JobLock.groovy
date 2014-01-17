// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import java.util.Date


/**
 * This is for jobs to ensure that only one machine is running a job at any given time
 * @author jwarner
 *
 */
class JobLock {
    String jobName
    Date lockAcquired = new Date()

    static constraints = {
        jobName unique: true
    }

    static mapping = {
        datasources(['DEFAULT', 'readReplica'])
    }
}
