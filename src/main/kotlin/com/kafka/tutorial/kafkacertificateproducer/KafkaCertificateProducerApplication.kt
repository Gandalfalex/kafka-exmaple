package com.kafka.tutorial.kafkacertificateproducer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaCertificateProducerApplication

fun main(args: Array<String>) {
    runApplication<KafkaCertificateProducerApplication>(*args)
}
