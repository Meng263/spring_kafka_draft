package com.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class MyConsumer {

    public final AtomicBoolean GOT_MESSAGE = new AtomicBoolean(false);

    @KafkaListener(topics = "my-topic")
    public void processMyTopicMessage(String message) {
        GOT_MESSAGE.set(true);
        log.info("Received message: {}", message);
    }

}
