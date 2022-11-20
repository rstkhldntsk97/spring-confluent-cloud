package io.confluent.developer.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

//@EnableKafkaStreams
@SpringBootApplication
public class SpringCCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCCloudApplication.class, args);
    }

}
