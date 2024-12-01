package com.kafka.tutorial.kafkaproducer.domain_objects

import java.util.*

data class CustomData(
    val id: UUID,
    val name: String,
    val value: String
)
