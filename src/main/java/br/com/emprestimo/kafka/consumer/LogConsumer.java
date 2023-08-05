package br.com.emprestimo.kafka.consumer;

import br.com.emprestimo.dtos.LogRequestDto;
import br.com.emprestimo.services.LogService;
import br.com.emprestimo.utils.ParserConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class LogConsumer {

    private final LogService service;

    private final ParserConfig parser;

    @KafkaListener(topics = "log.topic", groupId = "group-id")
    public void handleMessage(String message) {
        try {
            var parsedMessage = parser.getParser().fromJson(message, LogRequestDto.class);
            service.handleLogMessage(parsedMessage);
        } catch (Exception e) {
            log.error("Error whilhe parse message"); //IGNORING MESSAGE
        }

    }

}
