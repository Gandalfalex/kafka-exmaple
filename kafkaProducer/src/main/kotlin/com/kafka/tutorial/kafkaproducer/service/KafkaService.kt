package com.kafka.tutorial.kafkaproducer.service

import com.kafka.tutorial.kafkaproducer.domain_objects.CustomData
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class KafkaService (private val kafkaTemplate: KafkaTemplate<String, CustomData>) {


    private val logger = LoggerFactory.getLogger(KafkaService::class.java)

    fun sendMessage(topic: String, data: CustomData) {
        val sendMessage = kafkaTemplate.send(topic, data)
        logger.info("Send message: ${sendMessage.get(2, TimeUnit.SECONDS)}")
    }
}