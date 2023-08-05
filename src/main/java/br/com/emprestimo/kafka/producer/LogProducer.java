package br.com.emprestimo.kafka.producer;

import br.com.emprestimo.dtos.LogRequestDto;
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
public class LogProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ParserConfig parser;

    @Autowired
    public LogProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLog(LogRequestDto log) {
        var parsedMessage = parser.getParser().toJson(log);
        kafkaTemplate.send(Topics.LOG_TOPIC.getTopicName(), parsedMessage);
    }
}
