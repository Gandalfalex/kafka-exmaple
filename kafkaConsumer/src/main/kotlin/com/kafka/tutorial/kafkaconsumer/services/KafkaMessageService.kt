package com.kafka.tutorial.kafkaconsumer.services

import com.kafka.tutorial.kafkaconsumer.domain_objects.CustomData
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaMessageService(@Autowired val publisher: PubSubPublisher) {

    private val logger = LoggerFactory.getLogger(KafkaMessageService::class.java)

    @KafkaListener(topics = ["\${kafka.topic}"],
        groupId="static",
        containerFactory = "kafkaListenerContainerFactory")
    fun onMessage(data: CustomData) {
        logger.info("Received message: {}", data)

        publisher.publishMessage(data, "response-topic")
    }


}