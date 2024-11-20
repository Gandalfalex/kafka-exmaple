package com.kafka.tutorial.kafkacertificateproducer.controller

import com.kafka.tutorial.kafkacertificateproducer.domain_objects.CustomData
import com.kafka.tutorial.kafkacertificateproducer.service.KafkaService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class KafkaController(
    private val kafkaProducerService: KafkaService
) {

    @GetMapping("/send")
    fun sendMessage(@RequestParam topic: String, @RequestParam message: String) {
        val customMessage = CustomData(UUID.randomUUID(),"temp", message)
        kafkaProducerService.sendMessage(topic, customMessage)
    }
}