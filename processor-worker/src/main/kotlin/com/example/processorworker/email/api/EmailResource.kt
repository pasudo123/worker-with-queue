package com.example.processorworker.email.api

import org.springframework.validation.annotation.Validated
import java.time.LocalDateTime
import java.time.ZoneId
import javax.validation.constraints.Email

class EmailResource {

    @Validated
    data class Request(
        @field:Email
        val email: String,
        val subject: String,
        val body: String,
        val dateTime: LocalDateTime,
        val zoneId: ZoneId
    )

    data class Response(
        val success: Boolean,
        val message: String,
        val jobId: String? = null,
        val jobGroup: String? = null
    )
}