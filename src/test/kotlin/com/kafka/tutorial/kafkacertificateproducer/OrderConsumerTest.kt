package com.kafka.tutorial.kafkacertificateproducer

import com.kafka.tutorial.kafkacertificateproducer.domain_objects.CustomData
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile
import java.io.IOException
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals


@SpringBootTest
@Testcontainers
//@ContextConfiguration(classes = [TestKafkaConfiguration::class, KafkaConsumerConfig::class, KafkaProducerConfig::class])
class OrderConsumerTest {

    companion object {
        private const val TOPIC = "test-topic"
        private val messages = LinkedBlockingQueue<CustomData>()

        @Container
        @JvmStatic
        private val kafkaContainer: KafkaContainer = KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.7.1")
        )
            .withEnv("KAFKA_LISTENERS", "SSL://0.0.0.0:9093,PLAINTEXT://0.0.0.0:9092,BROKER://0.0.0.0:9091")
            .withEnv("KAFKA_ADVERTISED_LISTENERS", "SSL://localhost:9093,PLAINTEXT://localhost:9092,BROKER://localhost:9091")
            .withEnv("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP", "SSL:SSL,PLAINTEXT:PLAINTEXT,BROKER:PLAINTEXT")
            .withEnv("KAFKA_SSL_KEYSTORE_LOCATION", "/etc/kafka/secrets/kafka.keystore.jks")
            .withEnv("KAFKA_SSL_KEYSTORE_PASSWORD", "test1234")
            .withEnv("KAFKA_SSL_KEY_PASSWORD", "test1234")
            .withEnv("KAFKA_SSL_TRUSTSTORE_LOCATION", "/etc/kafka/secrets/kafka.truststore.jks")
            .withEnv("KAFKA_SSL_TRUSTSTORE_PASSWORD", "test1234")
            .withCopyFileToContainer(
                MountableFile.forClasspathResource("kafka.keystore.jks"),
                "/etc/kafka/secrets/kafka.keystore.jks"
            )
            .withCopyFileToContainer(
                MountableFile.forClasspathResource("kafka.truststore.jks"),
                "/etc/kafka/secrets/kafka.truststore.jks"
            )
            .withExposedPorts(9093)
            .withEmbeddedZookeeper()
            //.apply { start() }

        @JvmStatic
        @DynamicPropertySource
        fun kafkaProperties(registry: DynamicPropertyRegistry) {
            val mappedPort = kafkaContainer.getMappedPort(9093)
            val bootstrapServers = "SSL://localhost:$mappedPort"

            registry.add("spring.kafka.bootstrap-servers") { bootstrapServers }
            registry.add("spring.kafka.security.protocol") { "SSL" }
            registry.add("spring.kafka.ssl.key-password") { "test1234" }
            registry.add("spring.kafka.ssl.trust-store-password") { "test1234" }
            registry.add("spring.kafka.ssl.key-store-password") { "test1234" }
            registry.add("spring.kafka.ssl.trust-store-location") {
               // resourceFilePath("client.truststore.jks")
                "client.truststore.jks"
            }
            registry.add("spring.kafka.ssl.key-store-location") {
                //resourceFilePath("client.keystore.jks")
                "client.keystore.jks"
            }
        }

        @Throws(IOException::class)
        private fun resourceFilePath(resource: String): String {
            return ClassPathResource(resource).file.absolutePath
        }
    }

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, CustomData>

    @Test
    fun `test order consumer receives message`() {
        val order = CustomData(id= UUID.randomUUID(), name ="test", value = "test")
        kafkaTemplate.send(TOPIC, order)
        val receivedOrder = messages.poll(1, TimeUnit.SECONDS)
        assertEquals(order, receivedOrder)
    }

    @KafkaListener(topics = [TOPIC], containerFactory = "kafkaListenerContainerFactoryRegularImport")
    fun listen(order: CustomData) {
        messages.offer(order)
    }
}
