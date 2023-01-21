package br.com.emprestimo.services;

import br.com.emprestimo.domain.LoanEntity;
import br.com.emprestimo.domain.LoanPaymentsEntity;
import br.com.emprestimo.dtos.LoanRequest;
import br.com.emprestimo.exception.InvalidLoanTimeFrameException;
import br.com.emprestimo.exception.PaymentNotFoundException;
import br.com.emprestimo.exception.UserAlreadyHasUnpayLoansException;
import br.com.emprestimo.repositories.LoanPaymentsRepository;
import br.com.emprestimo.repositories.LoanRepository;
import br.com.emprestimo.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanService {

    private static final Long THIRTY_YEARS_IN_DAYS = 10958L;

    private final LoanRepository repository;
    private final LoanPaymentsRepository loanPaymentsRepository;
    private final UserRepository userRepository;
    private final Environment env;

    @Transactional
    public void requestLoan(LoanRequest request) {
        loanIsEligible(request.getLoanValue(),request.getLoanDateSigned(),request.getLoanDateDue());
        validateMaxLoanTime(request);
        var user = userRepository.findUserByCpf(request.getUserCpf());
        if (user.isPresent() && user.get().getIsUserActive()) {
            var loan = new LoanEntity(request);
            loan.setUser(user.get());
            log.info("Saving loan {}",loan.getLoanId());
            var loanSaved = repository.save(loan);
            log.info("Loan saved {}",loan.getLoanId());
            var loanPayments = createLoanPayments(loanSaved);
            loanPaymentsRepository.saveAll(loanPayments);
            log.info("All {} payments referent to the loan {} is created",loanPayments.size(),loan.getLoanId());
        } else {
            throw new UnsupportedOperationException("error while save loan");
        }
    }
    @Transactional
    public void updateLoanStatus(UUID loanId, String loanStatus) {
        var loan = repository.findById(loanId);
        if (loan.isPresent() && loan.get().getUser().getIsUserActive()) {
            loan.get().setIsApproved(Boolean.valueOf(loanStatus));
            repository.save(loan.get());
        } else {
            throw new UnsupportedOperationException("loan not found or user is not activated");
        }
    }

    @Transactional
    public void payLoan(UUID loanId) {
        var loan = repository.findById(loanId).orElseThrow(() -> new PaymentNotFoundException("Loan not found"));
        var loanPayments = loanPaymentsRepository.findAllByLoan(loan);
        if (loan.getIsApproved() && loan.getUser().getIsUserActive()) {
            loan.setIsPayed(Boolean.TRUE);
            loanPayments.forEach(it -> {
                it.setIsPayed(Boolean.TRUE);
                it.setPaymentDay(LocalDate.now());
            });
            repository.save(loan);
            log.info("Loan {} payed",loan.getLoanId());
            loanPaymentsRepository.saveAll(loanPayments);
            log.info("All {} payments referent do Loan {} is payed",loanPayments.size(),loan.getLoanId());
        } else {
            log.error("Error while pay loan = {}",loanId);
            throw new UnsupportedOperationException("error to pay loan");
        }
    }

    private void loanIsEligible(BigDecimal loanValue, String dateSign, String dateDue) {
        var loans = repository.findLoanByValueAndDates(loanValue,LocalDate.parse(dateSign),LocalDate.parse(dateDue),false);
        if (!loans.isEmpty()) {
            throw new UserAlreadyHasUnpayLoansException("User already has unpay loans");
        }
    }

    private List<LoanPaymentsEntity> createLoanPayments(LoanEntity loan) {
        var monthsToDue = (int) ChronoUnit.MONTHS.between(loan.getLoanDateSigned().withDayOfMonth(1),loan.getLoanDateDue().withDayOfMonth(1));
        var loanPayments = new ArrayList<LoanPaymentsEntity>();
        var monthlyValue = (loan.getLoanValue().toBigInteger().doubleValue() / monthsToDue) +
                ((loan.getLoanValue().toBigInteger().doubleValue() / monthsToDue) * getInterestValue());
        for (int i = 0; i < monthsToDue ; i++) {
            var loanPayment = new LoanPaymentsEntity();
            loanPayment.setLoan(loan);
            loanPayment.setPaymentSupposedPayDay(LocalDate.now().plusMonths(i).withDayOfMonth(1));
            loanPayment.setPaymentValue(monthlyValue);
            loanPayments.add(loanPayment);
        }
        return loanPayments;
    }

    private Double getInterestValue() {
        return Double.valueOf(Objects.requireNonNull(env.getProperty("loan.interest.value")));
    }

    private void validateMaxLoanTime(LoanRequest loan) {
        var loanTotalDays = ChronoUnit
                .DAYS
                .between(LocalDate.parse(loan.getLoanDateSigned()),
                        LocalDate.parse(loan.getLoanDateDue()).plusDays(1L));
        if (loanTotalDays > THIRTY_YEARS_IN_DAYS) {
            log.warn("Loan has a max time of 30 years");
            throw new InvalidLoanTimeFrameException("Loan has a max time of 30 years");
        }
    }

}
