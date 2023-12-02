package br.com.emprestimo.services;

import br.com.emprestimo.domain.LoanEntity;
import br.com.emprestimo.domain.LoanPaymentsEntity;
import br.com.emprestimo.dtos.LoanRequest;
import br.com.emprestimo.enums.Topics;
import br.com.emprestimo.exception.*;
import br.com.emprestimo.kafka.producer.KafkaSender;
import br.com.emprestimo.repositories.LoanPaymentsRepository;
import br.com.emprestimo.repositories.LoanRepository;
import br.com.emprestimo.utils.UserContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanService extends UserContextUtil {

    private static final Long THIRTY_YEARS_IN_DAYS = 10958L;

    private final LoanRepository repository;
    private final LoanPaymentsRepository loanPaymentsRepository;
    private final KafkaSender sender;

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
            this.sendMessageToQueue(loanSaved.getLoanId().toString(), Topics.CREATE_PAYMENTS_TOPIC.getTopicName());
        } else {
            throw new UnsupportedOperationException("error while save loan");
        }
    }

    @Transactional
    public void updateLoanStatus(UUID loanId, String loanStatus) {
        var user = getUser();

        var loan = repository.findById(loanId).orElseThrow(() -> new PaymentNotFoundException("Loan not found"));

        if (!Objects.equals(loan.getUser().getId(), user.getId())) {
            throw new UserNotFoundException("you cannot update anothers user loan");
        }

        if (Boolean.TRUE.equals(user.getIsUserActive())) {
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
        var payedInstallments = loanPayments.stream().filter(LoanPaymentsEntity::getIsPayed).collect(Collectors.toUnmodifiableList());

        if (!payedInstallments.isEmpty()) {
            log.warn("The loan with id -> {} already has payed installments", loanId);
            throw new AlreadyPayException("Are you sure thats ok?");
        }

        if (loan.getIsApproved() && loan.getUser().getIsUserActive()) {
            loan.setIsPayed(Boolean.TRUE);
            loan.setValueAlreadyPayed(BigDecimal.valueOf(loanPayments.stream().mapToDouble(LoanPaymentsEntity::getPaymentValue).reduce(Double::sum).orElseThrow()));
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

    private void sendMessageToQueue(String id, String topic) {
        sender.sendMessage(id, topic);
    }

}
