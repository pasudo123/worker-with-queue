package com.example.processorworker.exwithjob

import org.quartz.Scheduler
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("index")
class IndexController(
    private val indexService: IndexService,
    private val scheduler: Scheduler,
) {

    @GetMapping("ex/{message}")
    fun handleJob(
        @PathVariable("message") message: String
    ): ResponseEntity<String> {

        val jobDetail = indexService.buildJobDetail(message)
        val trigger = indexService.buildTrigger(jobDetail)

        scheduler.scheduleJob(jobDetail, trigger)

        return ResponseEntity.ok("ok")
    }
}