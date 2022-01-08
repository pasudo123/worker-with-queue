package com.example.processorworker

import org.quartz.spi.TriggerFiredBundle
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.scheduling.quartz.SpringBeanJobFactory

/**
 * job 인스턴스를 빈으로 사전에 등록하기 위함.
 */
class AutowiringSpringBeanJobFactory : SpringBeanJobFactory(), ApplicationContextAware {

    // not serialized
    @Transient
    private lateinit var beanFactory: AutowireCapableBeanFactory

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        beanFactory = applicationContext.autowireCapableBeanFactory
    }

    @Throws(Exception::class)
    override fun createJobInstance(bundle: TriggerFiredBundle): Any {
        val job = super.createJobInstance(bundle).also {
            beanFactory.autowireBean(it)
        }

        return job
    }
}