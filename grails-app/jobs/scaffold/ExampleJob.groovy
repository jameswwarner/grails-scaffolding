// Copyright (c) 2013-2014 James W. Warner, All Rights Reserved

package scaffold

/**
 * Example job.
 * @author jwarner
 *
 */
class ExampleJob extends ExclusiveJob {
    def jobHistoryService

    // For now, hourly at 40m past
    static triggers = {
        cron name: 'exampleTrigger', cronExpression: "0 40 * * * ?"
    }

    /**
     * Override to get the job name to ensure exclusivity
     */
    def getJobName() {
        return "ExampleJob"
    }

    /**
     * Main job logic
     */
    def runJob() {
        log.error "Running "+getJobName()
        def jobHistory = jobHistoryService.captureStart(getJobName())
        // do the stuff
        jobHistoryService.captureEnd(jobHistory)
        return
    }
}

