package com.example.processorworker

import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.quartz.JobListener
import org.slf4j.LoggerFactory

/**
 * quartz 잡 실행 여부에 따라 이후 호출되는 메소드
 * - 별도의 알람이 가는게 좋을듯하다.
 */
class CommonJobListener : JobListener {

    companion object {
        private const val TO_BE_EXECUTE = "TO_BE_EXECUTE"
        private const val EXECUTION_VETOED = "EXECUTION_VETOED"
        private const val WAS_EXECUTED = "WAS_EXECUTED"
    }

    private val log = LoggerFactory.getLogger(javaClass)

    override fun getName(): String {
        return "=== [CommonJobListener] ==="
    }

    /**
     * 잡이 실행할때 호출됨 (tobe -> was executed)
     */
    override fun jobToBeExecuted(context: JobExecutionContext?) {
        loggingListen(TO_BE_EXECUTE, context)
    }

    /**
     * 잡이 실행되지 않았을 때(거부되었을 때) 호출됨
     */
    override fun jobExecutionVetoed(context: JobExecutionContext?) {
        loggingListen(EXECUTION_VETOED, context)
    }

    /**
     * 잡이 실행되고 난 뒤 호출됨 (tobe -> was executed)
     */
    override fun jobWasExecuted(context: JobExecutionContext?, jobException: JobExecutionException?) {
        loggingListen(WAS_EXECUTED, context, jobException)
    }

    private fun loggingListen(currentStatus: String, context: JobExecutionContext? = null, jobException: JobExecutionException? = null) {
        val lines = StringBuilder()
        lines.appendLine()
        lines.appendLine("====> listener [$currentStatus] start ====>")
        context?.also {
            lines.appendLine("- jobKey :: ${context.jobDetail.key}")
            lines.appendLine("- jobData :: ${context.mergedJobDataMap}")
        }
        jobException?.also { ex ->
            lines.appendLine("- exception-message : ${ex.message}")
        }
        lines.appendLine("====> listener [$currentStatus] end ====>")
        lines.appendLine()

        log.info(lines.toString())
    }
}