package br.com.emprestimo;

import br.com.caelum.stella.validation.InvalidStateException;
import br.com.emprestimo.domain.UserEntity;
import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.exception.InvalidCPFException;
import br.com.emprestimo.exception.UserNotFoundException;
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
class UserSignUpTest {

    private static String USER_TEST_WRONG_CPF = "1998";
    private static String USER_TEST_CPF = "78142440644";
    private static String USER_TEST_EMAIL = "email@email.com";
    private static String USER_TEST_NAME = "Nome teste";

    private static final String USER_TEST_PWD = "teste";

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
        request.setPassword(USER_TEST_PWD);
        var user1 = new UserEntity(request);

       assertThrows(InvalidCPFException.class, () -> CpfValidation.validateCpf(user1.getCpf()));
    }

    @Test
    void should_save_user_when_no_exception_is_thrown() {
        var request = new UserSignUpRequest();
        request.setCpf(USER_TEST_CPF);
        request.setEmail(USER_TEST_EMAIL);
        request.setName(USER_TEST_NAME);
        request.setPassword(USER_TEST_PWD);
        var user1 = new UserEntity(request);
        service.signUpUser(request);

        var userSaved = repository.findUserByCpf(request.getCpf());
        Assertions.assertNotEquals(Optional.empty(),userSaved);
        Assertions.assertEquals(user1.getCpf(),userSaved.get().getCpf());
    }

    @Test
    void should_thrown_exception_when_update_user_not_found() {
        var request = new UserSignUpRequest();
        request.setCpf(USER_TEST_CPF);
        request.setEmail(USER_TEST_EMAIL);
        request.setName(USER_TEST_NAME);
        request.setPassword(USER_TEST_PWD);
        var user1 = new UserEntity(request);
        service.signUpUser(request);

        assertThrows(UserNotFoundException.class, () -> service.updateUserStatus(USER_TEST_WRONG_CPF,"TRUE"));

    }
}
