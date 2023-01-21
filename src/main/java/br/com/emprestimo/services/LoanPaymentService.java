package br.com.emprestimo.services;

import br.com.emprestimo.exception.PaymentNotFoundException;
import br.com.emprestimo.repositories.LoanPaymentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanPaymentService {
    private final LoanPaymentsRepository loanPaymentsRepository;

    public void payInstalment(UUID paymentId) {
        var payment = loanPaymentsRepository.findByPaymentIdAndIsPayed(paymentId,Boolean.FALSE).orElseThrow(
                ()-> new PaymentNotFoundException("Payment " +paymentId+ " not found or its already payed"));

        payment.setIsPayed(Boolean.TRUE);
        payment.setPaymentDay(LocalDate.now());
        loanPaymentsRepository.save(payment);
        log.info("Instalment {} payed, value = {}", paymentId,payment.getPaymentValue());
    }

}
