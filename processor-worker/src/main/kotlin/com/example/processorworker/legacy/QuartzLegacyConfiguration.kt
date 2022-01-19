package com.example.processorworker.legacy

import org.quartz.JobDetail
import org.quartz.Trigger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.quartz.QuartzProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import java.util.*

/**
 * [JobDetail]
 * JobDetail 을 이용하여 실제 Job 을 정의할 수 있다.
 * 아래는 SampleJob 을 정의하고 있는 두가지 방식을 보여주고 있다.
 * - 1) build style 로 제공
 * - 2) bean style 로 제공
 *
 * [Trigger]
 * Trigger 를 이용하여 Job 실행에 대한 메카니즘을 제공할 수 있다. (언제 잡이 실행될 것인지?)
 * Trigger 인스턴스는 Job execute 를 fire 한다. 두 가지 방식으로 트리거를 설정한다.
 * - 1) Trigger Builder
 * - 2) Spring's SimpleTriggerFactoryBean
 *
 * [JobStore]
 * JobStore 는 Job & Trigger 의 저장소 메카니즘을 제공한다.
 * 작업 스케줄과 관련된 데이터를 유지하기 위한 책임을 가지고 있다.
 * 인메모리 방식도 있고 persistence 방식도 있다.
 * - 1) spring.quartz.job-store-type: memory
 * - 2) spring.quartz.job-store-type: jdbc
 *  - JdbcStore 은 트랜잭션 처리에 대한 두가지 방식이 있다.
 *  - JobStoreTX(자체 트랜잭션 처리) & JobStoreCMT(애플리케이션 단 트랜잭션 처리)
 *
 * [Schedule]
 * Scheduler 는 Job Schedule 를 구현하기 위한 메인 인터페이스다.
 * SchedulerFactory 를 상속받고, 처음 생성할 시에 Jobs 와 Triggers 를 등록한다.
 * - 1) Quartz StdScheduleFactory 로 제공되는 방식
 * - 2) Spring ScheduleFactoryBean 로 제공되는 방식
 */
@Configuration
@ConfigurationProperties(prefix = "spring.quartz.properties")
class QuartzLegacyConfiguration(
    private val applicationContext: ApplicationContext,
    private val quartzProperties: QuartzProperties
) {

    private val log = LoggerFactory.getLogger(javaClass)

//    @Bean
//    fun jobDetailFactoryBean(): JobDetailFactoryBean {
//        log.info("#### JobDetailFactoryBean ####")
//        // bean style : JobDetail 인스턴스를 빈으로 제공해준다.
//        return JobDetailFactoryBean().apply {
//            this.setJobClass(SampleJob::class.java)
//            this.setDescription("invoke sample job service...")
//            this.setDurability(true)
//        }
//    }
//
//    @Bean
//    fun simpleTriggerFactoryBean(jobDetail: JobDetail): SimpleTriggerFactoryBean {
//        log.info("#### simpleTriggerFactoryBean ####")
//        return SimpleTriggerFactoryBean().apply {
//            this.setStartDelay(2000L)
//            this.setJobDetail(jobDetail)
//            // TODO 주기는 단위가 얼마인가?
//            this.setRepeatInterval(3600000)
//            // 무한반복
//            this.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)
//        }
//    }
//
//    @Bean
//    fun scheduleFactoryBean(trigger: Trigger, jobDetail: JobDetail): SchedulerFactoryBean {
//        log.info("#### scheduleFactoryBean ####")
//        return SchedulerFactoryBean().apply {
//            this.setQuartzProperties(quartzProperties.properties.toProperties())
//            this.setJobFactory(springBeanJobFactory())
//            this.setJobDetails(jobDetail)
//            this.setTriggers(trigger)
//        }
//    }
//
//    @Bean
//    fun springBeanJobFactory(): SpringBeanJobFactory {
//        log.info("#### springBeanJobFactory ####")
//        return AutowiringSpringBeanJobFactory().apply {
//            this.setApplicationContext(applicationContext)
//        }
//    }
}