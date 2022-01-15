package com.example.processorworker.email

import javax.validation.constraints.Email

class EmailResource {

    data class Request(
        @field:Email
        val email: String,
        val subject: String,
        val body: String
    )

    data class Response(
        val success: Boolean,
        val message: String,
        val jobId: String? = null,
        val jobGroup: String? = null
    )
}