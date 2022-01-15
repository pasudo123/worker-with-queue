package com.example.processorworker.push.job

import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component

@Component
class PushJob : QuartzJobBean() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun executeInternal(context: JobExecutionContext) {
        val jobDataMap = context.mergedJobDataMap
        val contextInfos = """
            {
                "JobDataMap": "${jobDataMap.toMap()}",
                "JobGroup": "${context.jobDetail.key.group}",
                "JobName": "${context.jobDetail.key.name}",
                "JobDescription": "${context.jobDetail.description}"
            }
        """.trimIndent()

        val lines = StringBuilder()
        lines.appendLine()
        lines.appendLine("========== pushJob execute block ==========")
        lines.appendLine(contextInfos)
        lines.appendLine("===========================================")
        lines.appendLine()

        log.info(lines.toString())
    }
}