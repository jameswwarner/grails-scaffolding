// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

/**
 * Capturing job history so we know start and end times of jobs, so we can keep
 *  track of when jobs run and how long they take. Jobs can keep track of their
 *  timing by using the JobHistoryService.
 * @author jwarner
 */
class JobHistory {
    String jobName
    String hostName             // best guess of which machine the job is running on
    Date started = new Date()
    Date finished

    static constraints = {
        finished nullable: true
    }

    static mapping = {
        datasources(['DEFAULT', 'readReplica'])
    }
}
