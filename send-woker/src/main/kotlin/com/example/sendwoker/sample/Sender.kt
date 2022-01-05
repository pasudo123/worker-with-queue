package com.example.sendwoker.sample

import com.example.sendwoker.sample.ListenerConfiguration.Companion.TOPIC_EXCHANGE
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class Sender(
    private val rabbitTemplate: RabbitTemplate,
    private val receiver: Receiver
) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        log.info("sending message ...")
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, "foo.bar.baz", "Hello pasudo")
        log.info("call 1")
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS)
        log.info("call 2")
    }
}