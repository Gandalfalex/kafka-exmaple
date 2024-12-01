package com.kafka.tutorial.kafkaproducer.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders
import com.kafka.tutorial.kafkaproducer.domain_objects.CustomData
import org.slf4j.LoggerFactory
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service

@Service
class PubSubResponseReceiver {

    private val logger = LoggerFactory.getLogger(PubSubResponseReceiver::class.java)
    private val objectMapper = jacksonObjectMapper()

    @ServiceActivator(inputChannel = "pubsubResponseInputChannel")
    fun receiveResponse(payload: String,
                        @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) message: BasicAcknowledgeablePubsubMessage
    ) {
        try {
            val responseData: CustomData = objectMapper.readValue(payload)
            logger.info("Response received! Payload: $responseData")

            // Process the response as needed
            message.ack()
        } catch (e: Exception) {
            logger.error("Error processing response message: $payload", e)
        }
    }
}
