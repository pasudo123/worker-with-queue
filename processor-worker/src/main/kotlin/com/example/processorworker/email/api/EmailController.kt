package com.example.processorworker.email.api

import com.example.processorworker.email.service.EmailService
import org.quartz.Scheduler
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime
import javax.validation.Valid

@RestController
@RequestMapping("email")
class EmailController(
    private val emailService: EmailService,
    private val scheduler: Scheduler
) {

    private val log = LoggerFactory.getLogger(javaClass)


    @PostMapping
    fun addEmailSchedule(
        @Valid @RequestBody emailRequest: EmailResource.Request
    ): ResponseEntity<EmailResource.Response> {
        try {
            val dateTime = ZonedDateTime.of(emailRequest.dateTime, emailRequest.zoneId)

            if (dateTime.isBefore(ZonedDateTime.now())) {
                val response = EmailResource.Response(false, "datetime must be after current time.")

                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response)
            }

            val jobDetail = emailService.buildJobDetail(emailRequest)
            val trigger = emailService.buildTrigger(jobDetail, dateTime)

            scheduler.scheduleJob(jobDetail, trigger)

            val response = EmailResource.Response(
                true,
                message = "Email Scheduled Successfully!!",
                jobId = jobDetail.key.name,
                jobGroup = jobDetail.key.group
            )

            return ResponseEntity.ok(response)

        } catch (exception: Exception) {
            log.error("email schedule error : $exception")
            val response = EmailResource.Response(false, "error email schedule : ${exception.message}")

            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response)
        }
    }
}