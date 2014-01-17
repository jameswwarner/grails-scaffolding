// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import scaffold.JobHistory;

/**
 * Service for creating/updating JobHistory to keep
 *   track of when jobs run and how long they take.
 * @author jwarner
 */
class JobHistoryService {

    /**
     * Helper function to get host name
     * @return
     */
    private def getHostName() {
        InetAddress addr = InetAddress.getLocalHost();
        return "${addr.getCanonicalHostName()} (${addr.getHostAddress()})"
    }

    /**
     * Capture the start of the given job
     * @param jobName
     * @return
     */
    def captureStart(def jobName) {
        def hostName = getHostName()
        def jobHistory = new JobHistory(jobName: jobName, hostName: hostName)
        jobHistory.save()
        return jobHistory
    }

    /**
     * Takes the jobHistory object created by capture start, ends it and saves.
     * @param jobHistory
     * @return
     */
    def captureEnd(def jobHistory) {
        jobHistory.finished = new Date()
        jobHistory.save()
        return jobHistory
    }

}

