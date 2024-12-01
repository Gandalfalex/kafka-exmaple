package com.kafka.tutorial.kafkaconsumer.config

import com.kafka.tutorial.kafkaconsumer.domain_objects.CustomData
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
    @Value("\${spring.kafka.ssl.enabled}") private val sslEnabled: Boolean,
) {
    @Bean
    fun regularImportFactory(): ConsumerFactory<String, CustomData> {
        val props = mutableMapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ConsumerConfig.GROUP_ID_CONFIG to "static",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to ErrorHandlingDeserializer::class.java,
            ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS to JsonDeserializer::class.java.name,
            ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG to true,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to true,
            JsonDeserializer.TRUSTED_PACKAGES to "*",
            JsonDeserializer.VALUE_DEFAULT_TYPE to CustomData::class.java.name,
            JsonDeserializer.REMOVE_TYPE_INFO_HEADERS to true
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


        return DefaultKafkaConsumerFactory(props.toMap())
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, CustomData> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, CustomData>()
        factory.consumerFactory = regularImportFactory()
        return factory
    }
}