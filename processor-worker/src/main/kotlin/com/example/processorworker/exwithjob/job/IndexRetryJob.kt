package com.example.processorworker.exwithjob.job

import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.LoggerFactory
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

/**
 * https://examples.javacodegeeks.com/enterprise-java/quartz/java-quartz-best-practices-example/
 */
@Component
class IndexRetryJob : QuartzJobBean() {

    private val log = LoggerFactory.getLogger(javaClass)
    private val sequence = AtomicInteger(0)

    override fun executeInternal(context: JobExecutionContext) {
        val jobDataMap = context.mergedJobDataMap
        val message = jobDataMap["message"]

        try {
            // 에러 발생
            log.info("[0] message : $message")
            val result = 10 / 0
            log.info("===> 여기까지 로그가 도달하지 못한다. <===")
        } catch (exception: Exception) {
            log.error("[1] 에러 발생 : ${exception.message}")

            jobDataMap["message"] = "$message-${sequence.incrementAndGet()}"

            // JobExecutionException 을 만들고, 작업을 즉시 재실행한다.
            val executionException = JobExecutionException(exception)
            if (sequence.get() <= 3) {
                log.error("[2] JOB 을 retry 한다. : retry=${sequence.get()}")
                executionException.setRefireImmediately(true)
                throw executionException
            } else {
                log.error("[3] JOB 을 retry 를 끝낸다.")
            }
        }
    }
}