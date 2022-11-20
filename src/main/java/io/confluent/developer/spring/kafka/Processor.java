package io.confluent.developer.spring.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

//@Component
public record Processor() {

    @Autowired
    public void process(StreamsBuilder builder) {
        builder.stream("hobbit", Consumed.with(Serdes.Integer(),Serdes.String()))
                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
                .groupBy((k,v) -> v, Grouped.with(Serdes.String(),Serdes.String()))
                .count(Materialized.as("counts"))
                .toStream()
                .to("streams-word-count", Produced.with(Serdes.String(), Serdes.Long()));
    }

}
