package com.example.processorworker

import com.example.processorworker.config.CustomWorkerProps
import com.example.processorworker.sample.SampleJob
import com.example.processorworker.sample.SampleJobVersion2
import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.TriggerBuilder
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
        val jobDetail = buildJobBuilder()
        val jobDetailVersion2 = buildJobBuilderVersion2()

        if (scheduler.checkExists(jobDetail.key)) {
            // 존재하기 때문에 db 에서 들고와서 해당 잡을 수행한다.
            log.info("exist jobKey : ${jobDetail.key}")
            scheduler.deleteJob(jobDetail.key)
            scheduler.addJob(jobDetailVersion2, true, true)
            return
        }

        log.info("none exist jobKey : ${jobDetail.key}")
        // 미존재하는 경우만 새롭게 잡을 만든다.
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

    private fun buildJobBuilderVersion2(): JobDetail {
        return JobBuilder.newJob(SampleJobVersion2::class.java)
            .withIdentity(workerProps.simpleCronWorker.id, "sample job")
            .withDescription("sample job desc")
            .storeDurably(true)
            .build()
    }

    private fun buildTrigger(jobDetail: JobDetail): Trigger {

        return TriggerBuilder.newTrigger()
            .withIdentity(jobDetail.key.name, "sample trigger")
            .withDescription("sample trigger desc")
            .withSchedule(cronSchedule("0/3 * * * * ?"))
            .build()
    }
}

fun main(args: Array<String>) {
    runApplication<ProcessorWorkerApplication>(*args)
}
