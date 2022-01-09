package com.example.processorworker.email.service

import com.example.processorworker.email.api.EmailResource
import org.quartz.JobBuilder
import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.SimpleScheduleBuilder
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*

@Service
class EmailService {

    fun buildJobDetail(emailRequest: EmailResource.Request): JobDetail {
        val jobDataMap = JobDataMap().apply {
            this["email"] = emailRequest.email
            this["subject"] = emailRequest.subject
            this["body"] = emailRequest.body
        }

        return JobBuilder.newJob(EmailJob::class.java)
            .withIdentity(UUID.randomUUID().toString(), "email-jobs")
            .withDescription("Send Email Job")
            .usingJobData(jobDataMap)
            .storeDurably()
            .build()
    }

    fun buildTrigger(jobDetail: JobDetail, startAt: ZonedDateTime): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.key.name, "email-triggers")
            .withDescription("Send Email Trigger")
            .startAt(Date.from(startAt.toInstant()))
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
            .build()
    }
}