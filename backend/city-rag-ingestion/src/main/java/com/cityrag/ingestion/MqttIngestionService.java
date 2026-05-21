package com.cityrag.ingestion;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class MqttIngestionService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter("tcp://localhost:1883", "city-rag-ingestion",
                        "/city-rag/+/+/telemetry");
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        return adapter;
    }

    @Bean
    public IntegrationFlow mqttIngestionFlow() {
        return IntegrationFlows
                .from(mqttInbound())
                .handle(message -> {
                    String payload = message.getPayload().toString();
                    String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
                    log.info("Received MQTT message from topic={}, payload={}", topic, payload);
                    try {
                        // Forward to Kafka for processing
                        kafkaTemplate.send("telemetry-raw", topic, payload);
                    } catch (Exception e) {
                        log.error("Failed to forward MQTT message to Kafka", e);
                    }
                })
                .get();
    }
}
