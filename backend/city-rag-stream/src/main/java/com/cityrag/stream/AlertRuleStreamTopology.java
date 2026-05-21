package com.cityrag.stream;

import com.cityrag.service.alert.AlertRuleEngine;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Slf4j
@Configuration
public class AlertRuleStreamTopology {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Bean
    public KStream<String, String> alertRuleStream(StreamsBuilder streamsBuilder, AlertRuleEngine alertRuleEngine) {
        KStream<String, String> stream = streamsBuilder.stream(
                "telemetry-raw",
                Consumed.with(Serdes.String(), Serdes.String())
        );

        stream
                .peek((key, value) -> {
                    try {
                        // 解析 telemetry JSON，提取 resourceId 和 resourceType
                        Map<String, Object> data = MAPPER.readValue(value, Map.class);
                        String resourceId = key;
                        String resourceType = (String) data.getOrDefault("resourceType", "unknown");
                        Object payload = data.get("payload");

                        // 调用告警规则引擎进行评估
                        alertRuleEngine.onTelemetryData(resourceId, resourceType, payload);
                    } catch (Exception e) {
                        log.warn("Failed to process telemetry data: key={}, error={}", key, e.getMessage());
                    }
                })
                .to("alert-event", Produced.with(Serdes.String(), Serdes.String()));

        return stream;
    }
}
