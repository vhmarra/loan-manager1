package br.com.emprestimo.kafka.producer;

import br.com.emprestimo.enums.Topics;
import br.com.emprestimo.utils.ParserConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreatePaymentsKafkaSender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public CreatePaymentsKafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String loanId) {
        log.info("Sending message to topic -> {}", Topics.CREATE_PAYMENTS_TOPIC.getTopicName());
        kafkaTemplate.send(Topics.CREATE_PAYMENTS_TOPIC.getTopicName(), loanId);
    }
}