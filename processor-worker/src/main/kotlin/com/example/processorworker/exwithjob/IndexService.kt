package com.example.processorworker.exwithjob

import com.example.processorworker.buildIndexTypeJobDate
import com.example.processorworker.exwithjob.job.IndexRetryJob
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
class IndexService {

    fun buildJobDetail(message: String): JobDetail {
        val jobDataMap = buildIndexTypeJobDate(message)

        return JobBuilder.newJob(IndexRetryJob::class.java)
            .withIdentity(UUID.randomUUID().toString(), "index-jobs")
            .withDescription("Index Job Desc")
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
            .withIdentity(jobDetail.key.name, "index-triggers")
            .withDescription("Index Trigger")
            .startAt(Date.from(zonedDateTime.toInstant()))
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
            .build()
    }
}