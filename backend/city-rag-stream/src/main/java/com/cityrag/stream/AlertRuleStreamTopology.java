package com.cityrag.stream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlertRuleStreamTopology {

    @Bean
    public KStream<String, String> alertRuleStream(StreamsBuilder streamsBuilder) {
        KStream<String, String> stream = streamsBuilder.stream(
                "telemetry-raw",
                Consumed.with(Serdes.String(), Serdes.String())
        );

        stream
                .peek((key, value) -> {
                    // Telemetry data flows through to alert processing
                })
                .to("alert-event", Produced.with(Serdes.String(), Serdes.String()));

        return stream;
    }
}
