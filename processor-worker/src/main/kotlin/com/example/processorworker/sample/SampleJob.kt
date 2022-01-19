package com.example.processorworker.sample

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Job Interface 는 하나의 메소드를 제공 ex. execute()
 * 실제 잡이 수행되는 디테일한 내용을 구현해야 한다.
 * 만약 잡이 트리거 되어 실행되면, 스케줄러는 execute() 를 실행한다. 그리고 JobExecutionContext 전달한다.
 *
 * JobExecutionContext 는 런타임환경에서 잡 인스턴스에 대한 정보를 제공한다.
 * JobInstance 를 사전에 등록할 때, 생성자 주입을 할 시 에러가 발생한다.
 */
@Component
class SampleJob : Job {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun execute(context: JobExecutionContext) {
        val lines = StringBuilder()
        lines.appendLine()
        lines.appendLine("===================================")
        lines.appendLine("job : ${context.jobDetail.key.name}")
        lines.appendLine("fire-time : ${context.fireTime}")
        lines.appendLine("execute-time : ${context.nextFireTime}")
        lines.appendLine("===================================")
        lines.appendLine()

        log.info(lines.toString())
    }
}