package com.kafka.tutorial.kafkaproducer.controller

import com.kafka.tutorial.kafkaproducer.domain_objects.CustomData
import com.kafka.tutorial.kafkaproducer.service.KafkaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class KafkaController {

    @Autowired
    private lateinit var kafkaService: KafkaService

    @GetMapping("/kafka")
    fun kafka(): ResponseEntity<String> {
        kafkaService.sendMessage("test-topic",  CustomData(id= UUID.randomUUID(), value = "test",name="test"))

        return ResponseEntity.ok("OK")
    }
}