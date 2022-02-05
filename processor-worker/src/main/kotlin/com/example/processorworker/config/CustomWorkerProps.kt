package com.example.processorworker.config

import com.example.processorworker.sample.SampleJob
import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.ZoneId
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "custom-worker")
class CustomWorkerProps {

    val simpleCronWorker = SimpleCronWorker()

    class SimpleCronWorker {
        var id: String? = null
        var jobGroup: String = "default-group"
        var jobDesc: String = "default-desc"
        var triggerGroup: String = "default-trigger-group"
        var triggerDesc: String = "default-trigger-desc"
        var cronExpression: String? = null
    }


    @Bean("SimpleJobDetail")
    fun simpleJobDetail(): JobDetail {
        return JobBuilder.newJob().ofType(SampleJob::class.java)
            .withIdentity(simpleCronWorker.id!!, simpleCronWorker.jobGroup)
            .withDescription(simpleCronWorker.jobDesc)
            // trigger 에 의해 수행되지 않더라도(잡은 고아상태, orphaned) 잡 인스턴스는 남겨놓음 -> false 면 execute 가 완료되고 이후에 gc 에 의해서 처리됨
            .storeDurably(true)
            .requestRecovery(true) // 잡이 문제가 되는 상황에 대해서 리커버리를 할 지 여부를 결정한다.
            .build()
    }

    @Bean("SimpleJobTrigger")
    fun simpleJobTrigger(): Trigger {
        return TriggerBuilder.newTrigger()
            .withIdentity(simpleJobDetail().key.name, simpleCronWorker.triggerGroup)
            .withDescription(simpleCronWorker.triggerDesc)
            .withSchedule(
                // 5초마다 크론 실행
                CronScheduleBuilder
                    .cronSchedule(simpleCronWorker.cronExpression)
                    .inTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Seoul")))
            )
            .build()
    }

    @Bean
    @QuartzDataSource
    fun quartzDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }
}