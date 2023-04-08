package br.com.emprestimo.services;

import br.com.emprestimo.domain.LoanEntity;
import br.com.emprestimo.domain.LoanPaymentsEntity;
import br.com.emprestimo.exception.PaymentNotFoundException;
import br.com.emprestimo.repositories.LoanPaymentsRepository;
import br.com.emprestimo.repositories.LoanRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class LoanPaymentService {

    private final LoanPaymentsRepository loanPaymentsRepository;
    private final LoanRepository loanRepository;
    private final Environment env;

    public void payInstalment(UUID paymentId) {
        var payment = loanPaymentsRepository.findByPaymentIdAndIsPayed(paymentId,Boolean.FALSE).orElseThrow(
                ()-> new PaymentNotFoundException("Payment " +paymentId+ " not found or its already payed"));

        payment.setIsPayed(Boolean.TRUE);
        payment.setPaymentDay(LocalDate.now());
        loanPaymentsRepository.save(payment);
        log.info("Instalment {} payed, value = {}", paymentId,payment.getPaymentValue());
    }

    public void createLoanPayments(String loanId) {
        var loan = loanRepository.findById(UUID.fromString(loanId)).orElseThrow(() -> new PaymentNotFoundException("Loan not found"));
        var loanPayments = createLoanPayments(loan);
        loanPaymentsRepository.saveAll(loanPayments);
        log.info("All {} loan payments from loan -> {} is saved",loanPayments.size(),loanId);
    }

    public List<LoanPaymentsEntity> createLoanPayments(LoanEntity loan) {
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

}
