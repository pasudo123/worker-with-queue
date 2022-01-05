package com.example.sendwoker.sample

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch

@Component
class Receiver {

    private val latch: CountDownLatch = CountDownLatch(1)
    private val log = LoggerFactory.getLogger(javaClass)

    fun receiveMessage(message: String) {
        log.info("Received : {}", message)
        latch.countDown()
    }

    fun getLatch(): CountDownLatch {
        return latch
    }
}