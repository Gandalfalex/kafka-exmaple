package com.kafka.tutorial.kafkacertificateproducer.kafkaProducer

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaConsumerConfig(
    @Value("\${spring.kafka.bootstrap-servers}") private val bootstrapServers: String,
    @Value("\${spring.kafka.security.protocol}") private val securityProtocol: String,
    @Value("\${spring.kafka.ssl.key-password}") private val keyPw: String,
    @Value("\${spring.kafka.ssl.trust-store-password}") private val trustStorePw: String,
    @Value("\${spring.kafka.ssl.key-store-password}") private val keyStorePw: String,
    @Value("\${spring.kafka.ssl.trust-store-location}") private val trustStoreLocation: String,
    @Value("\${spring.kafka.ssl.key-store-location}") private val keyStoreLocation: String,
) {
    @Bean
    fun regularImportFactory(): ConsumerFactory<String, Order> {
        val props = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG to "static",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to ErrorHandlingDeserializer::class.java,
            ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS to JsonDeserializer::class.java.name,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to true,
            JsonDeserializer.TRUSTED_PACKAGES to "*",
            JsonDeserializer.VALUE_DEFAULT_TYPE to Order::class.java.name,
            "security.protocol" to securityProtocol,
            "ssl.truststore.location" to trustStoreLocation,
            "ssl.keystore.location" to keyStoreLocation,
            "ssl.keystore.password" to keyStorePw,
            "ssl.truststore.password" to trustStorePw,
            "ssl.key.password" to keyPw,
            "ssl.endpoint.identification.algorithm" to "",
        )

        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun kafkaListenerContainerFactoryRegularImport(): ConcurrentKafkaListenerContainerFactory<String, Order> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Order>()
        factory.consumerFactory = regularImportFactory()
        return factory
    }
}
