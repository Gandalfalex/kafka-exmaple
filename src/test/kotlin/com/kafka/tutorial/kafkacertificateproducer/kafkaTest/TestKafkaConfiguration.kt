package com.kafka.tutorial.kafkacertificateproducer.kafkaTest

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile
import java.io.IOException


//@TestConfiguration
class TestKafkaConfiguration {

//    @Bean(destroyMethod = "stop")
//    fun kafkaContainer(): KafkaContainer {
//        val kafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.7.1"))
//            .withEnv("KAFKA_LISTENERS", "SSL://0.0.0.0:9093,PLAINTEXT://0.0.0.0:9092,BROKER://0.0.0.0:9091")
//            .withEnv("KAFKA_ADVERTISED_LISTENERS", "SSL://localhost:9093,PLAINTEXT://localhost:9092,BROKER://localhost:9091")
//            .withEnv("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP", "SSL:SSL,PLAINTEXT:PLAINTEXT,BROKER:PLAINTEXT")
//            .withEnv("KAFKA_SSL_KEYSTORE_LOCATION", "/etc/kafka/secrets/kafka.keystore.jks")
//            .withEnv("KAFKA_SSL_KEYSTORE_PASSWORD", "test1234")
//            .withEnv("KAFKA_SSL_KEY_PASSWORD", "test1234")
//            .withEnv("KAFKA_SSL_TRUSTSTORE_LOCATION", "/etc/kafka/secrets/kafka.truststore.jks")
//            .withEnv("KAFKA_SSL_TRUSTSTORE_PASSWORD", "test1234")
//            .withCopyFileToContainer(
//                MountableFile.forClasspathResource("kafka.keystore.jks"),
//                "/etc/kafka/secrets/kafka.keystore.jks"
//            )
//            .withCopyFileToContainer(
//                MountableFile.forClasspathResource("kafka.truststore.jks"),
//                "/etc/kafka/secrets/kafka.truststore.jks"
//            )
//            .withExposedPorts(9093)
//            .withEmbeddedZookeeper()
//            .apply { start() }
//
//
//        val mappedPort = kafkaContainer.getMappedPort(9093)
//        val bootstrapServers = "SSL://localhost:$mappedPort"
//
//        kafkaContainer.addEnv("KAFKA_ADVERTISED_LISTENERS", "SSL://localhost:$mappedPort")
//
//
//        System.setProperty("spring.kafka.bootstrap-servers", bootstrapServers)
//        System.setProperty("spring.kafka.security.protocol", "SSL")
//        System.setProperty("spring.kafka.ssl.key-password", "test1234")
//        System.setProperty("spring.kafka.ssl.trust-store-password", "test1234")
//        System.setProperty("spring.kafka.ssl.key-store-password", "test1234")
//        System.setProperty("spring.kafka.ssl.trust-store-location", resourceFilePath("client.truststore.jks"))
//        System.setProperty("spring.kafka.ssl.key-store-location", resourceFilePath("client.keystore.jks"))
//
//
//        return kafkaContainer
//    }
//
//    @Throws(IOException::class)
//    private fun resourceFilePath(resource: String): String {
//        return ClassPathResource(resource).file.absolutePath
//    }
}
