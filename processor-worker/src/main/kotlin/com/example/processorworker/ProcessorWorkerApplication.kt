package com.example.processorworker

import org.quartz.Scheduler
import org.quartz.impl.matchers.EverythingMatcher.allJobs
import org.quartz.impl.matchers.EverythingMatcher.allTriggers
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProcessorWorkerApplication(
    private val scheduler: Scheduler,
    private val processorWorkerBuilder: ProcessorWorkerBuilder
) : CommandLineRunner {

    override fun run(vararg args: String) {
        processorWorkerBuilder.build()
        // 잡 리스너를 등록한다.
        this.addJobListeners()
        this.addTriggerListeners()
    }

    /**
     * 특정 잡에 대한 리스너를 등록할 수 있도록 한다.
     * 참고 : http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/tutorial-lesson-07.html
     */
    private fun addJobListeners() {
        scheduler.listenerManager.addJobListener(CommonJobListener(), allJobs())
    }

    /**
     * 트리거에 대한 리스너를 등록할 수 있다.
     * 참고 : http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/tutorial-lesson-07.html
     */
    private fun addTriggerListeners() {
        scheduler.listenerManager.addTriggerListener(CommonTriggerListener(), allTriggers())
    }
}

fun main(args: Array<String>) {
    runApplication<ProcessorWorkerApplication>(*args)
}
