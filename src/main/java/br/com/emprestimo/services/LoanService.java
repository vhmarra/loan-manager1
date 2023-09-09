package br.com.emprestimo.services;

import br.com.emprestimo.domain.LoanEntity;
import br.com.emprestimo.dtos.LoanRequest;
import br.com.emprestimo.exception.InvalidLoanTimeFrameException;
import br.com.emprestimo.exception.PaymentNotFoundException;
import br.com.emprestimo.exception.UserAlreadyHasUnpayLoansException;
import br.com.emprestimo.kafka.producer.CreatePaymentsKafkaSender;
import br.com.emprestimo.repositories.LoanPaymentsRepository;
import br.com.emprestimo.repositories.LoanRepository;
import br.com.emprestimo.repositories.UserRepository;
import br.com.emprestimo.utils.UserContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanService extends UserContextUtil {

    private static final Long THIRTY_YEARS_IN_DAYS = 10958L;

    private final LoanRepository repository;
    private final LoanPaymentsRepository loanPaymentsRepository;
    private final CreatePaymentsKafkaSender kafkaSender;

    @Transactional
    public void requestLoan(LoanRequest request) {
        loanIsEligible(request.getLoanValue(), request.getLoanDateSigned(), request.getLoanDateDue());
        validateMaxLoanTime(request);
        var user = getUser();
        if (user.getIsUserActive()) {
            var loan = new LoanEntity(request);
            loan.setUser(user);
            log.info("Saving loan {}", loan.getLoanId());
            var loanSaved = repository.save(loan);
            log.info("Loan saved {}", loanSaved.getLoanId());
            kafkaSender.sendMessage(loanSaved.getLoanId().toString());
        } else {
            throw new UnsupportedOperationException("error while save loan");
        }
    }

    @Transactional
    public void updateLoanStatus(UUID loanId, String loanStatus) {
        var loan = repository.findById(loanId).orElseThrow(() -> new PaymentNotFoundException("Loan not found"));
        if (Boolean.TRUE.equals(loan.getUser().getIsUserActive())) {
            loan.setIsApproved(Boolean.valueOf(loanStatus));
            repository.save(loan);
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
            log.info("Loan {} payed", loan.getLoanId());
            loanPaymentsRepository.saveAll(loanPayments);
            log.info("All {} payments referent do Loan {} is payed", loanPayments.size(), loan.getLoanId());
        } else {
            log.error("Error while pay loan = {}", loanId);
            throw new UnsupportedOperationException("error to pay loan");
        }
    }

    private void loanIsEligible(BigDecimal loanValue, String dateSign, String dateDue) {
        var loans = repository.findLoanByValueAndDates(loanValue, LocalDate.parse(dateSign), LocalDate.parse(dateDue), false);
        if (!loans.isEmpty()) {
            throw new UserAlreadyHasUnpayLoansException("User already has unpay loans");
        }
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
