package com.kafka.tutorial.kafkaproducer.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.cloud.spring.pubsub.core.PubSubTemplate
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders
import com.kafka.tutorial.kafkaproducer.domain_objects.CustomData
import com.kafka.tutorial.kafkaproducer.domain_objects.WorkingData
import org.slf4j.LoggerFactory
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PubSubProcessor(private val pubSubTemplate: PubSubTemplate) {

    private val logger = LoggerFactory.getLogger(PubSubProcessor::class.java)
    private val objectMapper = jacksonObjectMapper()

    @ServiceActivator(inputChannel = "pubsubInputChannel")
    fun processMessage(payload: String,
                       @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) message: BasicAcknowledgeablePubsubMessage
    ) {
        try {
            // Deserialize the incoming message
            val incomingData: CustomData = objectMapper.readValue(payload)
            logger.info("Received data: $incomingData")

            // Perform your business logic here
            val processedData = performBusinessLogic(incomingData)

            // Publish the response to the response topic
            val responseTopic = "response-topic"
            val responseJson = objectMapper.writeValueAsString(processedData)
            pubSubTemplate.publish(responseTopic, responseJson)

            // Acknowledge the incoming message
            message.ack()
        } catch (e: Exception) {
            logger.error("Error processing message: $payload", e)
        }
    }

    private fun performBusinessLogic(data: CustomData): WorkingData {
        // Example logic: Modify the incoming data or perform some computation
        return WorkingData(id=data.id, value = data.value, name=data.name, timestamp = LocalDateTime.now())
    }
}