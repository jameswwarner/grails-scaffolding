// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

import scaffold.JobLock;
import grails.util.Environment


/**
 * Abstract class for making a job exclusive - only one at a time across all machines.
 * @author jwarner
 *
 */
abstract class ExclusiveJob {

    // gets the job name, must override
    abstract def getJobName()

    abstract def runJob()

    /**
     * The execute method of the job, ensures this is the only oen running, then
     * calls the abstract runJob method for the meat of the logic.
     * @return
     */
    def execute() {
        // jobs run in every env but production. For prod, they run in custom prodJobs env
        if (Environment.current != Environment.PRODUCTION) {
            // first, let's make sure we're the only machine running
            JobLock lock
            JobLock.withTransaction {
                lock = new JobLock(jobName: getJobName())
                lock.save(insert: true, validate: true, flush: true, failOnError: true)
            }

            try {
                runJob()
            }
            catch (Exception e) {
                // We just swallow it and move on
                log.error("Swallowed exception. ", e)
            }

            JobLock.withTransaction {
                // delete the lock
                lock.delete()
            }
        }
    }
}
