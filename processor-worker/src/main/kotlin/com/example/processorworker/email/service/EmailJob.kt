package com.example.processorworker.email.service

import org.quartz.JobExecutionContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.quartz.QuartzJobBean
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import javax.mail.internet.MimeMessage

@Component
class EmailJob : QuartzJobBean() {

    @Autowired
    private lateinit var mailSender: JavaMailSender

    @Autowired
    private lateinit var mailProperties: MailProperties

    private val log = LoggerFactory.getLogger(javaClass)

    override fun executeInternal(context: JobExecutionContext) {
        val jobDataMap = context.mergedJobDataMap

        val subject: String = jobDataMap["subject"] as String
        val body: String = jobDataMap["body"] as String
        val recipientEmail: String = jobDataMap["email"] as String

        sendEmail(
            mailProperties.username,
            recipientEmail,
            subject = subject,
            body
        )
    }

    private fun sendEmail(from: String, to: String, subject: String, body: String) {
        try {
            val message: MimeMessage = mailSender.createMimeMessage()

            MimeMessageHelper(message, StandardCharsets.UTF_8.toString())
                .also { helper ->
                    helper.setSubject(subject)
                    helper.setText(body, true)
                    helper.setFrom(from)
                    helper.setTo(to)
                }

            mailSender.send(message)
            log.info("mail send success")
        } catch (exception: Exception) {
            log.error("send email error : ${exception.message}")
        }
    }
}