package com.kafka.tutorial.kafkaconsumer.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders
import com.kafka.tutorial.kafkaconsumer.domain_objects.CustomData
import org.slf4j.LoggerFactory
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service

@Service
class PubSubReceiver {

    private val logger = LoggerFactory.getLogger(PubSubReceiver::class.java)
    private val objectMapper = jacksonObjectMapper()

    @ServiceActivator(inputChannel = "pubsubInputChannel")
    fun messageReceiver(payload: String,
                        @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) message: BasicAcknowledgeablePubsubMessage
    ) {
        try {
            val myMessage: CustomData = objectMapper.readValue(payload)
            logger.info("Message arrived! Payload: $myMessage")
            message.ack()
        } catch (e: Exception) {
            logger.error("Could not deserialize message: $payload", e)
        }
    }
}