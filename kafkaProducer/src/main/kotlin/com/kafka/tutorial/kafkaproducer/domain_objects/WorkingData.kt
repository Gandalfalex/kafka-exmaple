package com.kafka.tutorial.kafkaproducer.domain_objects

import java.time.LocalDateTime
import java.util.*

data class WorkingData(
    val id: UUID,
    val name: String,
    val value: String,
    val timestamp: LocalDateTime,
)
