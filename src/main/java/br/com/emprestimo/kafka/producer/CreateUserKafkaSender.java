package br.com.emprestimo.kafka.producer;

import br.com.emprestimo.dtos.UserSignUpRequest;
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
public class CreateUserKafkaSender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ParserConfig parser;

    @Autowired
    public CreateUserKafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(UserSignUpRequest request) {
        log.info("Sending message to topic -> {}", Topics.CREATE_USER_TOPIC.getTopicName());

        var userRequestParsed = parser.getParser().toJson(request);

        kafkaTemplate.send(Topics.CREATE_USER_TOPIC.getTopicName(), userRequestParsed);
    }
}
