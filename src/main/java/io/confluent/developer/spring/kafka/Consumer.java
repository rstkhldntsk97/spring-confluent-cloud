package io.confluent.developer.spring.kafka;

import io.confluent.developer.avro.Hobbit;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public record Consumer() {

    @KafkaListener(topics = {"hobbit-avro"}, groupId = "spring-boot-kafka")
    public void consume(ConsumerRecord<Integer, Hobbit> record) {
        System.out.println("Received " + record.value() + " with key" + record.key());
    }

}