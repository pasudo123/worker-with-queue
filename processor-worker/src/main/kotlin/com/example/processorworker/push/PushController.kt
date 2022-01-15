package com.example.processorworker.push

import org.quartz.Scheduler
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("push")
class PushController (
    private val pushService: PushService,
    private val scheduler: Scheduler
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun sendPush(
        @RequestBody pushRequest: PushResource.Request
    ): ResponseEntity<PushResource.Response> {
        return try {
            val jobDetail = pushService.buildJobDetail(pushRequest)
            val trigger = pushService.buildTrigger(jobDetail)

            scheduler.scheduleJob(jobDetail, trigger)

            ResponseEntity.ok(PushResource.Response("ok"))
        } catch (exception: Exception) {
            log.error("push schedule error : $exception")

            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(PushResource.Response("failed"))
        }
    }
}