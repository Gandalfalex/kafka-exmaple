package com.kafka.tutorial.kafkacertificateproducer.kafkaProducer

import com.kafka.tutorial.kafkacertificateproducer.domain_objects.CustomData
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig(
    @Value("\${spring.kafka.bootstrap-servers}") private val bootstrapServers: String,
    @Value("\${spring.kafka.security.protocol}") private val securityProtocol: String,
    @Value("\${spring.kafka.ssl.key-password}") private val keyPw: String,
    @Value("\${spring.kafka.ssl.trust-store-password}") private val trustStorePw: String,
    @Value("\${spring.kafka.ssl.key-store-password}") private val keyStorePw: String,
    @Value("\${spring.kafka.ssl.trust-store-location}") private val trustStoreLocation: String,
    @Value("\${spring.kafka.ssl.key-store-location}") private val keyStoreLocation: String,
) {

    @Bean
    fun producerFactory(): ProducerFactory<String, CustomData> {
        val props = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
            "security.protocol" to securityProtocol,
            "ssl.truststore.location" to trustStoreLocation,
            "ssl.keystore.location" to keyStoreLocation,
            "ssl.keystore.password" to keyStorePw,
            "ssl.truststore.password" to trustStorePw,
            "ssl.key.password" to keyPw,
            "ssl.endpoint.identification.algorithm" to "",
        )

        return DefaultKafkaProducerFactory(props)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, CustomData> {
        return KafkaTemplate(producerFactory())
    }

}