package br.com.emprestimo.services;

import br.com.emprestimo.domain.LoanEntity;
import br.com.emprestimo.domain.LoanPaymentsEntity;
import br.com.emprestimo.exception.PaymentNotFoundException;
import br.com.emprestimo.repositories.LoanPaymentsRepository;
import br.com.emprestimo.repositories.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.FinanceLib;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanPaymentService {
    private final LoanPaymentsRepository loanPaymentsRepository;
    private final LoanRepository loanRepository;
    private final InterestService interestService;

    public void payInstalment(UUID paymentId) {
        var payment = loanPaymentsRepository.findByPaymentIdAndIsPayed(paymentId, Boolean.FALSE).orElseThrow(
                () -> new PaymentNotFoundException("Payment " + paymentId + " not found or its already payed"));

        payment.setIsPayed(Boolean.TRUE);
        payment.setPaymentDay(LocalDate.now());
        loanPaymentsRepository.save(payment);
        log.info("Instalment {} payed, value = {}", paymentId, payment.getPaymentValue());
    }

    public void createLoanPayments(String loanId) {
        var loan = loanRepository.findById(UUID.fromString(loanId)).orElseThrow(() -> new PaymentNotFoundException("Loan not found"));
        var loanPayments = createLoanPayments(loan);
        loanPaymentsRepository.saveAll(loanPayments);
        log.info("All {} loan payments from loan -> {} is saved", loanPayments.size(), loanId);
    }

    public List<LoanPaymentsEntity> createLoanPayments(LoanEntity loan) {
        var monthsToDue = ChronoUnit.MONTHS.between(loan.getLoanDateSigned().withDayOfMonth(1), loan.getLoanDateDue().withDayOfMonth(1));
        var monthsToDueCasted = Long.valueOf(monthsToDue);
        var loanPayments = new ArrayList<LoanPaymentsEntity>();

        var monthlyValue = (loan.getLoanValue().toBigInteger().doubleValue() / monthsToDue) +
                ((loan.getLoanValue().toBigInteger().doubleValue() / monthsToDue) * interestService.getInterestValue());
        for (int i = 0; i < monthsToDue; i++) {
            var loanPayment = new LoanPaymentsEntity();
            loanPayment.setLoan(loan);
            loanPayment.setPaymentSupposedPayDay(LocalDate.now().plusMonths(i).withDayOfMonth(1));
            generatePaymentValue(i, loanPayment, monthlyValue, loanPayments, monthsToDueCasted);
            loan.setIsPayed(false);
            loanPayments.add(loanPayment);
        }
        return loanPayments;
    }

    private void generatePaymentValue(int i, LoanPaymentsEntity loanPayment, double monthlyValue, ArrayList<LoanPaymentsEntity> loanPayments, Long monthsToDueCasted) {
        double lastPayment;
        if (i == 0) {
            loanPayment.setPaymentValue(monthlyValue);
        } else {
            lastPayment = loanPayments.get(i - 1).getPaymentValue();
            var fee = lastPayment * (interestService.getInterestValue() / monthsToDueCasted.doubleValue());
            loanPayment.setPaymentValue(lastPayment + fee);
        }
    }
}
