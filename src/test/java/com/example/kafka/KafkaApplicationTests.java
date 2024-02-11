package com.example.kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = KafkaApplicationTests.KafkaInitializer.class)
class KafkaApplicationTests {

	@Autowired
	MyConsumer myConsumer;

	@Test
	void contextLoads() {

		String bootstrapServers = kafkaContainer.getBootstrapServers();
		Producer<Void, String> producer = new DefaultKafkaProducerFactory<Void, String>(
				Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers),
				new VoidSerializer(),
				new StringSerializer()
		).createProducer();


		while (true) {
			producer.send(new ProducerRecord<>("my-topic", "hello"));
		}

//		Awaitility.await().until(() -> myConsumer.GOT_MESSAGE.get());


	}

	@Container
	public static KafkaContainer kafkaContainer = new KafkaContainer();

	static class KafkaInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			TestPropertyValues.of(
					"spring.kafka.consumer.bootstrap-servers:" + kafkaContainer.getBootstrapServers()
			).applyTo(applicationContext.getEnvironment());
		}
	}

}
