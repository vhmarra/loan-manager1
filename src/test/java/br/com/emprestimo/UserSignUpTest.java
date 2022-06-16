package br.com.emprestimo;

import br.com.emprestimo.domain.UserEntity;
import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.utils.CpfValidation;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
@ActiveProfiles("test")
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
