package com.example.processorworker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProcessorWorkerApplication

fun main(args: Array<String>) {
    runApplication<ProcessorWorkerApplication>(*args)
}
