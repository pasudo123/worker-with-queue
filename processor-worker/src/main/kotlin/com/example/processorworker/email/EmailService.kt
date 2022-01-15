package com.example.processorworker.email

import com.example.processorworker.buildEmailTypeJobData
import com.example.processorworker.email.job.EmailJob
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.SimpleScheduleBuilder
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Service
class EmailService {

    fun buildJobDetail(request: EmailResource.Request): JobDetail {
        val jobDataMap = buildEmailTypeJobData(request)

        return JobBuilder.newJob(EmailJob::class.java)
            .withIdentity(UUID.randomUUID().toString(), "email-jobs")
            .withDescription("Send Email Job")
            .usingJobData(jobDataMap)
            .storeDurably()
            .build()
    }

    fun buildTrigger(jobDetail: JobDetail): Trigger {
        val zonedDateTime = ZonedDateTime
            .now(ZoneId.of("Asia/Seoul"))
            .plusSeconds(10)

        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.key.name, "email-triggers")
            .withDescription("Send Email Trigger")
            .startAt(Date.from(zonedDateTime.toInstant()))
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
            .build()
    }
}