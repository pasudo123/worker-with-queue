package com.example.processorworker.sample

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger

@Service
class SampleJobService {

    private val log = LoggerFactory.getLogger(javaClass)
    private val count: AtomicInteger = AtomicInteger(0)

    companion object {
        const val EXECUTION_TIME = 2000L
    }

    fun executeSampleJob() {

        log.info("execute sample job start !!")

        try {
            Thread.sleep(EXECUTION_TIME)
        } catch (exception: Exception) {
            log.error("job error : {}", exception.message)
        } finally {
            count.incrementAndGet()
            log.info("execute sample job end !!")
        }
    }

    fun getNumberOfInvocation(): Int {
        return count.get()
    }
}