package com.example.processorworker.push

import com.example.processorworker.buildPushTypeJobData
import com.example.processorworker.push.job.PushJob
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
class PushService {

    fun buildJobDetail(request: PushResource.Request): JobDetail {
        val jobDataMap = buildPushTypeJobData(request)

        return JobBuilder.newJob(PushJob::class.java)
            .withIdentity(UUID.randomUUID().toString(), "push-jobs")
            .withDescription("Send Push Job")
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
            .withIdentity(jobDetail.key.name, "push-triggers")
            .withDescription("Send Push Trigger")
            .startAt(Date.from(zonedDateTime.toInstant()))
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
            .build()
    }
}