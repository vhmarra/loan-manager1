package br.com.emprestimo.configs;

import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.enums.Topics;
import br.com.emprestimo.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CreateUserKafkaConsumer {

    private final UserService service;

    @KafkaListener(topics = "create.user.topic",groupId = "group-id")
    void createUser(String request) {
        var user = UserSignUpRequest.fromString(request);
        log.info("Creating user -> cpf:{} name:{}",user.getCpf(),user.getName());
        service.signUpUser(user);
    }

}
