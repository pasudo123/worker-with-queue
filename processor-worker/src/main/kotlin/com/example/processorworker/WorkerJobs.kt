package com.example.processorworker

import com.example.processorworker.email.EmailResource
import com.example.processorworker.push.PushResource
import org.quartz.JobDataMap

fun buildIndexTypeJobDate(message: String): JobDataMap {
    return JobDataMap().apply {
        this["message"] = message
    }
}

fun buildPushTypeJobData(request: PushResource.Request): JobDataMap {
    return JobDataMap().apply {
        this["message"] = request.message
    }
}

fun buildEmailTypeJobData(request: EmailResource.Request): JobDataMap {
    return JobDataMap().apply {
        this["email"] = request.email
        this["subject"] = request.subject
        this["body"] = request.body
    }
}