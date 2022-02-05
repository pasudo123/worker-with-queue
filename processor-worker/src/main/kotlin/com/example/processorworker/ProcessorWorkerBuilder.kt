package com.example.processorworker

import com.example.processorworker.config.CustomWorkerProps
import org.quartz.CronTrigger
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.Trigger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class ProcessorWorkerBuilder(
    @Qualifier("SimpleJobDetail")
    private val simpleJobDetail: JobDetail,
    @Qualifier("SimpleJobTrigger")
    private val simpleJobTrigger: Trigger,
    private val scheduler: Scheduler,
    private val workerProps: CustomWorkerProps
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun build() {

        if (scheduler.checkExists(simpleJobDetail.key)) {
            log.info("=====> [1] 존재하는 잡 : ${simpleJobDetail.key}")

            if (this.isEqualCronExpression()) {
                log.info("=====> [1.1] 동일 크론 표현식. 그대로 리턴한다.")
                return
            }

            log.info("=====> [1.2] 다른 크론 표헌식. 해당 잡을 삭제한다.")
            scheduler.deleteJob(simpleJobDetail.key)
        }

        log.info("=====> [2] 잡을 스케줄링한다.")
        scheduler.scheduleJob(simpleJobDetail, simpleJobTrigger)
    }

    private fun isEqualCronExpression(): Boolean {
        val storeTriggers = scheduler.getTriggersOfJob(simpleJobDetail.key)

        for (trigger in storeTriggers) {
            if (trigger !is CronTrigger) {
                continue
            }

            return trigger.cronExpression ==  workerProps.simpleCronWorker.cronExpression
        }

        return false
    }
}