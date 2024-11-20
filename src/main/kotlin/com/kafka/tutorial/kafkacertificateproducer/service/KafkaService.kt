package com.kafka.tutorial.kafkacertificateproducer.service

import com.kafka.tutorial.kafkacertificateproducer.domain_objects.CustomData
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaService (private val kafkaTemplate: KafkaTemplate<String, CustomData>) {

    fun sendMessage(topic: String, data: CustomData) {
        kafkaTemplate.send(topic, data)//.addCallback({println("send successfully $data")}, {println(ex -> println(ex) )})
    }

    @KafkaListener(topics = ["\${kafka.topic}"], groupId="static", containerFactory = "kafkaListenerContainerFactory")
    fun onMessage(topic: String, data: CustomData) {}
}