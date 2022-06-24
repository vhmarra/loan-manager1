package br.com.emprestimo;

import br.com.emprestimo.domain.UserEntity;
import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.repositories.UserRepository;
import br.com.emprestimo.services.UserService;
import br.com.emprestimo.utils.CpfValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
@ActiveProfiles("test")
@SpringBootTest
public class UserSignUpTest {

    private static String USER_TEST_WRONG_CPF = "1998";
    private static String USER_TEST_CPF = "78142440644";
    private static String USER_TEST_EMAIL = "email@email.com";
    private static String USER_TEST_NAME = "Nome teste";

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repository;

    @Test
    void should_exception_when_violate_validation() {
        var request = new UserSignUpRequest();
        request.setCpf(USER_TEST_WRONG_CPF);
        request.setEmail(USER_TEST_EMAIL);
        request.setName(USER_TEST_NAME);
        var user1 = new UserEntity(request);

       assertThrows(UnsupportedOperationException.class, () -> CpfValidation.validateCpf(user1.getCpf()));
    }

    @Test
    void should_save_user_when_no_exception_is_thrown() {
        var request = new UserSignUpRequest();
        request.setCpf(USER_TEST_CPF);
        request.setEmail(USER_TEST_EMAIL);
        request.setName(USER_TEST_NAME);
        var user1 = new UserEntity(request);
        service.signUpUser(request);

        var userSaved = repository.findUserByCpf(request.getCpf());
        Assertions.assertNotEquals(Optional.empty(),userSaved);
        Assertions.assertEquals(user1.getCpf(),userSaved.get().getCpf());
    }





}
