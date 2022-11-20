package io.confluent.developer.spring.kafka;

import com.github.javafaker.Faker;
import io.confluent.developer.avro.Hobbit;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class Producer {

    private final KafkaTemplate<Integer, Hobbit> kafkaTemplate;

    Faker faker;

    @EventListener(ApplicationStartedEvent.class)
    public void generateData() {
        faker = Faker.instance();
        final Flux<Long> interval = Flux.interval(Duration.ofMillis(1000));
        final Flux<String> quotes = Flux.fromStream(Stream.generate(() -> faker.hobbit().quote()));
        Flux.zip(interval, quotes)
                .map((Function<Tuple2<Long, String>, Object>) it -> kafkaTemplate.send(new ProducerRecord<>("hobbit-avro", faker.random().nextInt(42), new Hobbit(it.getT2()))))
                .blockLast();
    }

}