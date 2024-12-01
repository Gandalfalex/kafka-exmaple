package com.kafka.tutorial.kafkaproducer.config

import com.kafka.tutorial.kafkaproducer.domain_objects.CustomData
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
    @Value("\${spring.kafka.ssl.enabled}") private val sslEnabled: Boolean,
) {

    @Bean
    fun producerFactory(): ProducerFactory<String, CustomData> {
        val props = mutableMapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
            JsonSerializer.ADD_TYPE_INFO_HEADERS to false
            )
        if (sslEnabled) {
            props["security.protocol"] = securityProtocol
            props["ssl.truststore.location"] = trustStoreLocation
            props["ssl.keystore.location"] = keyStoreLocation
            props["ssl.keystore.password"] = keyStorePw
            props["ssl.truststore.password"] = trustStorePw
            props["ssl.key.password"] = keyPw
            props["ssl.endpoint.identification.algorithm"] = ""
        }

        return DefaultKafkaProducerFactory(props.toMap())
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, CustomData> {
        return KafkaTemplate(producerFactory())
    }

}