package io.confluent.developer.spring.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @KafkaListener(topics = {"hobbit"}, groupId = "spring-boot-kafka")
    public void consume(ConsumerRecord<Integer, String> record) {
        System.out.println("Received quote with key = " + record.key() + " and value = " + record.value());
    }

}