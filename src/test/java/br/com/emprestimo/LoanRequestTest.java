package br.com.emprestimo;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.emprestimo.domain.LoanEntity;
import br.com.emprestimo.domain.UserEntity;
import br.com.emprestimo.dtos.LoanRequest;
import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.exception.UserAlreadyHasUnpayLoansException;
import br.com.emprestimo.repositories.LoanPaymentsRepository;
import br.com.emprestimo.repositories.LoanRepository;
import br.com.emprestimo.repositories.UserRepository;
import br.com.emprestimo.services.LoanPaymentService;
import br.com.emprestimo.services.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
public class LoanRequestTest {

    private static final String USER_TEST_WRONG_CPF = "1998";
    private static final String USER_TEST_CPF = "78142440644";
    private static final String USER_TEST_EMAIL = "email@email.com";
    private static final String USER_TEST_NAME = "Nome teste";

    private static final String USER_TEST_PWD = "teste";

    private static final LocalDate TODAY = LocalDate.now();

    private static final LocalDate TODAY_PLUS_30 = TODAY.plusDays(30L);

    @Autowired
    private LoanService service;

    @Autowired
    private LoanRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanPaymentsRepository loanPaymentsRepository;

    @Autowired
    private LoanPaymentService loanPaymentService;

    @Test
    void should_not_save_loan_when_user_is_not_active() {
        var request = new UserSignUpRequest();
        request.setCpf(USER_TEST_CPF);
        request.setEmail(USER_TEST_EMAIL);
        request.setName(USER_TEST_NAME);
        request.setPassword(USER_TEST_PWD);
        var savedUser = saveUser(request,Boolean.FALSE);
        var loanRequest = buildLoanRequest(BigDecimal.valueOf(3000L),TODAY,TODAY_PLUS_30,savedUser.getCpf());

        assertThrows(UnsupportedOperationException.class, () -> service.requestLoan(loanRequest));
    }

    @Test
    void should_not_save_loan_when_user_already_have_loans(){
        var request = new UserSignUpRequest();
        request.setCpf(USER_TEST_CPF);
        request.setEmail(USER_TEST_EMAIL);
        request.setName(USER_TEST_NAME);
        request.setPassword(USER_TEST_PWD);
        var savedUser = saveUser(request,Boolean.TRUE);
        var loanRequestOne = buildLoanRequest(BigDecimal.valueOf(3000L),TODAY,TODAY_PLUS_30,savedUser.getCpf());
        var loanRequestTwo = buildLoanRequest(BigDecimal.valueOf(3000L),TODAY,TODAY_PLUS_30,savedUser.getCpf());
        repository.save(buildLoanEntity(loanRequestOne,savedUser));

        assertThrows(UserAlreadyHasUnpayLoansException.class, () -> service.requestLoan(loanRequestTwo));
    }

    @Test
    void should_create_loan_payments_when_create_loan() {
        var request = new UserSignUpRequest();
        request.setCpf(USER_TEST_CPF);
        request.setEmail(USER_TEST_EMAIL);
        request.setName(USER_TEST_NAME);
        request.setPassword(USER_TEST_PWD);
        var savedUser = saveUser(request,Boolean.TRUE);
        var loanRequest = buildLoanRequest(BigDecimal.valueOf(3000L),TODAY,TODAY_PLUS_30.plusMonths(11L),savedUser.getCpf());
        var savedLoan = repository.save(buildLoanEntity(loanRequest,savedUser));
        var loanPayments = loanPaymentService.createLoanPayments(savedLoan);

        assertEquals(12,loanPayments.size());
        loanPayments.forEach(it -> {
            assertEquals(savedLoan.getLoanId(),it.getLoan().getLoanId());
        });
    }

    private UserEntity saveUser(UserSignUpRequest user, Boolean userStatus) {
        var request = new UserSignUpRequest();
        request.setCpf(user.getCpf());
        request.setEmail(user.getEmail());
        request.setName(user.getName());
        request.setPassword(user.getPassword());
        var user1 = new UserEntity(request);
        user1.setIsUserActive(userStatus);
        return userRepository.save(user1);
    }

    private LoanRequest buildLoanRequest(BigDecimal loanValue, LocalDate loanDateSigned, LocalDate loanDateDue,
                                         String userCpf) {
        return new LoanRequest(loanValue,loanDateSigned.toString(),loanDateDue.toString(),userCpf);
    }

    private LoanEntity buildLoanEntity(LoanRequest request, UserEntity user) {
        var loanEntity = new LoanEntity(request);
        loanEntity.setUser(user);
        return loanEntity;
    }

}
