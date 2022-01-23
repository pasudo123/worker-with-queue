package com.example.processorworker

import com.example.processorworker.config.CustomWorkerProps
import com.example.processorworker.sample.SampleJob
import com.example.processorworker.sample.SampleJobVersion2
import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.quartz.impl.matchers.EverythingMatcher.allJobs
import org.quartz.impl.matchers.GroupMatcher.jobGroupEquals
import org.quartz.impl.matchers.OrMatcher.or
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProcessorWorkerApplication(
    private val workerProps: CustomWorkerProps,
    private val scheduler: Scheduler
) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String) {
        // 잡 리스너를 등록한다.
        this.addJobListeners()

        // 잡을 만든다.
        val jobDetail = buildJobBuilder()
        val jobDetailVersion2 = buildJobBuilderVersion2()

        // TODO("실행이 되질 않는다..? -> why??")
        if (scheduler.checkExists(jobDetail.key)) {
            // 존재하기 때문에 db 에서 들고와서 해당 잡을 수행한다.
            val lines = StringBuilder()
            lines.appendLine()
            lines.appendLine("++++++++++++++ exist jobKey : ${jobDetail.key} ++++++++++++++")
            lines.appendLine("job deleted :: ${scheduler.deleteJob(jobDetail.key)}")
            scheduler.addJob(jobDetailVersion2, true, true)
            log.info(lines.toString())
            return
        }

        log.info("++++++++++++++ none exist jobKey : ${jobDetail.key} ++++++++++++++")
        // 미존재하는 경우만 새롭게 잡을 만든다.
        val cronTrigger = this.buildTrigger(jobDetail)
        scheduler.scheduleJob(jobDetail, cronTrigger)
    }

    private fun buildJobBuilder(): JobDetail {
        return JobBuilder.newJob(SampleJob::class.java)
            .withIdentity(workerProps.simpleCronWorker.id, "sample job")
            .withDescription("sample job desc")
            .storeDurably(false)
            .build()
    }

    private fun buildJobBuilderVersion2(): JobDetail {
        return JobBuilder.newJob(SampleJobVersion2::class.java)
            .withIdentity(workerProps.simpleCronWorker.id, "sample job")
            .withDescription("sample job desc")
            .storeDurably(true)
            .build()
    }

    private fun buildTrigger(jobDetail: JobDetail): Trigger {

        return TriggerBuilder.newTrigger()
            .withIdentity(jobDetail.key.name, "sample trigger")
            .withDescription("sample trigger desc")
            .withSchedule(cronSchedule("0/3 * * * * ?"))
            .build()
    }

    private fun addJobListeners() {
        // http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/tutorial-lesson-07.html
        scheduler.listenerManager.addJobListener(CommonJobListener(), allJobs())
        // scheduler.listenerManager.addJobListener(CommonJobListener(), or(jobGroupEquals("sample job"), jobGroupEquals("sample job")))
    }
}

fun main(args: Array<String>) {
    runApplication<ProcessorWorkerApplication>(*args)
}
