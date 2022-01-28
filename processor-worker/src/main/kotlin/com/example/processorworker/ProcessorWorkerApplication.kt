package com.example.processorworker

import com.example.processorworker.config.CustomWorkerProps
import com.example.processorworker.sample.SampleJob
import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.quartz.impl.matchers.EverythingMatcher.allJobs
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProcessorWorkerApplication(
    private val workerProps: CustomWorkerProps,
    private val scheduler: Scheduler
) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String) {
        // 잡 리스너를 등록한다.
        this.addJobListeners()

        // 잡을 만든다.
        val jobDetail = buildJobBuilder()

        if (scheduler.checkExists(jobDetail.key)) {
            // 존재하기 때문에 db 에서 들고와서 해당 잡을 수행한다.
            val lines = StringBuilder()
            scheduler.deleteJob(jobDetail.key)
            lines.appendLine()
            lines.appendLine("[1] : ${jobDetail.key} ++++++++++++++")
            log.info(lines.toString())
        }

        log.info("[2] : ${jobDetail.key} ++++++++++++++")
        val cronTrigger = this.buildTrigger(jobDetail)
        scheduler.scheduleJob(jobDetail, cronTrigger)
    }

    private fun buildJobBuilder(): JobDetail {
        return JobBuilder.newJob(SampleJob::class.java)
            .withIdentity(workerProps.simpleCronWorker.id, "sample job")
            .withDescription("sample job desc")
            .storeDurably(false)
            .build()
    }

    private fun buildTrigger(jobDetail: JobDetail): Trigger {

        return TriggerBuilder.newTrigger()
            .withIdentity(jobDetail.key.name, "sample trigger")
            .withDescription("sample trigger desc")
            .withSchedule(cronSchedule("0/10 * * * * ?"))
            .build()
    }

    private fun addJobListeners() {
        // http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/tutorial-lesson-07.html
        scheduler.listenerManager.addJobListener(CommonJobListener(), allJobs())
        // scheduler.listenerManager.addJobListener(CommonJobListener(), or(jobGroupEquals("sample job"), jobGroupEquals("sample job")))
    }
}

fun main(args: Array<String>) {
    runApplication<ProcessorWorkerApplication>(*args)
}
