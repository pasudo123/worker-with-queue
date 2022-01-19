package com.example.processorworker

import com.example.processorworker.sample.SampleJob
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@SpringBootApplication
class ProcessorWorkerApplication(
    private val scheduler: Scheduler
) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String) {
        val jobDetail = buildJobBuilder()
        val cronTrigger = this.buildTrigger(jobDetail)

        scheduler.scheduleJob(jobDetail, cronTrigger)
    }

    private fun buildJobBuilder(): JobDetail {
        return JobBuilder.newJob(SampleJob::class.java)
            .withIdentity(UUID.randomUUID().toString(), "sample job")
            .withDescription("sample job desc")
            .storeDurably(true)
            .build()
    }

    private fun buildTrigger(jobDetail: JobDetail): Trigger {

        return TriggerBuilder.newTrigger()
            .withIdentity(jobDetail.key.name, "sample trigger")
            .withDescription("sample trigger desc")
            .startNow()
            .withSchedule(CronScheduleBuilder.cronSchedule("* * * ? * *"))
            .build()
    }
}

fun main(args: Array<String>) {
    runApplication<ProcessorWorkerApplication>(*args)
}
