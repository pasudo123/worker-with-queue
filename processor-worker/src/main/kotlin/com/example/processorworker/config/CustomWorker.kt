package com.example.processorworker.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "custom-worker")
class CustomWorkerProps {

    val simpleCronWorker = SimpleCronWorker()

    class SimpleCronWorker {
        lateinit var id: String
    }
}