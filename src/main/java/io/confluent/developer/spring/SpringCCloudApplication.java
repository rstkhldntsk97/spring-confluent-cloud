package io.confluent.developer.spring;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.function.Function;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringCCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCCloudApplication.class, args);
    }

}

@RequiredArgsConstructor
@Component
class Producer {

    private final KafkaTemplate<Integer, String> kafkaTemplate;

    Faker faker;

    @EventListener(ApplicationStartedEvent.class)
    public void generateData() {
        faker = Faker.instance();
        final Flux<Long> interval = Flux.interval(Duration.ofMillis(1000));
        final Flux<String> quotes = Flux.fromStream(Stream.generate(() -> faker.hobbit().quote()));
        Flux.zip(interval, quotes)
                .map((Function<Tuple2<Long, String>, Object>) it -> kafkaTemplate.send("hobbit", faker.random().nextInt(42), it.getT2()))
                .blockLast();
    }

}

@Component
class Consumer {

    @KafkaListener(topics = {"hobbit"}, groupId = "spring-boot-kafka")
    public void consume(ConsumerRecord<Integer, String> record) {
        System.out.println("Received quote with key = " + record.key() + " and value = " + record.value());
    }

}