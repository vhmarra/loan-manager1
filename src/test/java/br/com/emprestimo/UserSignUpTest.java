package br.com.emprestimo;

import br.com.emprestimo.domain.UserEntity;
import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.repositories.UserRepository;
import br.com.emprestimo.services.UserService;
import br.com.emprestimo.utils.CpfValidation;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class UserSignUpTest {

    private static String USER_TEST_WRONG_CPF = "1998";
    private static String USER_TEST_CPF = "78142440644";
    private static String USER_TEST_EMAIL = "email@email.com";
    private static String USER_TEST_NAME = "Nome teste";

    @Test
    void should_exception_when_violate_validation() {
        var request = new UserSignUpRequest();
        request.setCpf(USER_TEST_WRONG_CPF);
        request.setEmail(USER_TEST_EMAIL);
        request.setName(USER_TEST_NAME);
        var user1 = new UserEntity(request);

       assertThrows(UnsupportedOperationException.class, () -> CpfValidation.validateCpf(user1.getCpf()));
    }

}
