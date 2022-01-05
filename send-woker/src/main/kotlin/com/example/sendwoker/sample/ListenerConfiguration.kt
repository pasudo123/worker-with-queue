package com.example.sendwoker.sample

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ListenerConfiguration {

    companion object {
        const val TOPIC_EXCHANGE = "pasudo-topic"
        const val QUEUE = "pasudo-queue"
    }

    // queue() 는 AMQP queue 를 만든다.
    // 서버 재시동 시, 큐를 남기려면, durable true 로 한다.
    @Bean
    fun queue(): Queue {
        return Queue(QUEUE, false)
    }

    // exchange() 는 topicExchange 를 만든다.
    @Bean
    fun exchange(): TopicExchange {
        return TopicExchange(TOPIC_EXCHANGE)
    }

    // binding() 은 queue 와 exchange 두가지를 같이 바인딩한다.
    // RabbitTemplate 의 동작을 정의한다.
    @Bean
    fun binding(queue: Queue, exchange: TopicExchange): Binding {
        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#")
    }

    @Bean
    fun container(
        connectionFactory: ConnectionFactory,
        listenerAdapter: MessageListenerAdapter
    ): SimpleMessageListenerContainer {

        val container = SimpleMessageListenerContainer()

        return container.apply {
            this.connectionFactory = connectionFactory
            this.setQueueNames(QUEUE)
            this.setMessageListener(listenerAdapter)
        }
    }

    // container 에 메시지 리스너를 등록한다.
    // 해당 리스너는 `pasudo-test-queue` 를 수신대기 한다.
    // receiver 는 POJO 로 설정되어 있고, MessageListenerAdapter 로 래핑되어 있다. (Receiver 의 receiverMessage() 를 호출하도록 되어있다.
    // 메소드 네이밍도 같이 결정하는듯 하다.
    @Bean
    fun listenerAdapter(receiver: Receiver): MessageListenerAdapter {
        return MessageListenerAdapter(receiver, "receiveMessage")
    }
}