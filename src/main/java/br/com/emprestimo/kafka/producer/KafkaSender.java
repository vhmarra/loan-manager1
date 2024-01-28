package br.com.emprestimo.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message, String topic) {
        log.info("Sending message -> {} to topic -> {}", message, topic);
        kafkaTemplate.send(topic, message);
    }

    public void sendToDLQ(String message, String dlqTopic) {
        log.info("Sending message -> {} to dlq topic -> {}", message, dlqTopic);
        this.sendMessage(message, dlqTopic);
    }
}