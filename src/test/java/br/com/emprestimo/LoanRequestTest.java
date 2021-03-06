package br.com.emprestimo;

import br.com.emprestimo.domain.UserEntity;
import br.com.emprestimo.dtos.LoanRequest;
import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.repositories.LoanRepository;
import br.com.emprestimo.repositories.UserRepository;
import br.com.emprestimo.services.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
public class LoanRequestTest {

    private static final String USER_TEST_WRONG_CPF = "1998";
    private static final String USER_TEST_CPF = "78142440644";
    private static final String USER_TEST_EMAIL = "email@email.com";
    private static final String USER_TEST_NAME = "Nome teste";

    private static final LocalDate TODAY = LocalDate.now();

    private static final LocalDate TODAY_PLUS_30 = TODAY.plusDays(30L);

    @Autowired
    private LoanService service;

    @Autowired
    private LoanRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_not_save_loan_when_user_is_not_active() {
        var request = new UserSignUpRequest();
        request.setCpf(USER_TEST_CPF);
        request.setEmail(USER_TEST_EMAIL);
        request.setName(USER_TEST_NAME);
        var savedUser = saveUser(request,Boolean.FALSE);
        var loanRequest = buildLoanRequest(BigDecimal.valueOf(3000L),TODAY,TODAY_PLUS_30,savedUser.getCpf());

        assertThrows(UnsupportedOperationException.class, () -> service.requestLoan(loanRequest));
    }


    private UserEntity saveUser(UserSignUpRequest user, Boolean userStatus) {
        var request = new UserSignUpRequest();
        request.setCpf(user.getCpf());
        request.setEmail(user.getEmail());
        request.setName(user.getName());
        var user1 = new UserEntity(request);
        user1.setIsUserActive(userStatus);
        return userRepository.save(user1);
    }

    private LoanRequest buildLoanRequest(BigDecimal loanValue, LocalDate loanDateSigned, LocalDate loanDateDue,
                                         String userCpf) {
        return new LoanRequest(loanValue,loanDateSigned.toString(),loanDateDue.toString(),userCpf);
    }

}
