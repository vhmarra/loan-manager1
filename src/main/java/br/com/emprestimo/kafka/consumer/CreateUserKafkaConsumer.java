package br.com.emprestimo.kafka.consumer;

import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.services.UserService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CreateUserKafkaConsumer {

    private final UserService service;

    @KafkaListener(topics = "create.user.topic", groupId = "group-id")
    void createUser(String request) {
        var parsedUser = new Gson().fromJson(request, UserSignUpRequest.class);
        log.info("Creating user -> cpf:{} name:{}", parsedUser.getCpf(), parsedUser.getName());
        service.signUpUser(parsedUser);
    }

}
