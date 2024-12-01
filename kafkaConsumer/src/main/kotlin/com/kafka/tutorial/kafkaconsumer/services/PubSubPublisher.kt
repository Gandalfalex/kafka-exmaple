package com.kafka.tutorial.kafkaconsumer.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.cloud.spring.pubsub.core.PubSubTemplate
import com.kafka.tutorial.kafkaconsumer.domain_objects.CustomData
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PubSubPublisher(private val pubSubTemplate: PubSubTemplate) {

    private val logger = LoggerFactory.getLogger(PubSubPublisher::class.java)
    private val objectMapper = jacksonObjectMapper()

    fun publishMessage(myMessage: CustomData, topicName: String) {
        try {
            val jsonString = objectMapper.writeValueAsString(myMessage)
            pubSubTemplate.publish(topicName, jsonString)
            logger.info("Published message to topic $topicName")
        } catch (e: Exception) {
            logger.error("Could not serialize message: $myMessage", e)
        }
    }
}