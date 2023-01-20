package br.com.emprestimo.services;

import br.com.emprestimo.exception.PaymentFoundException;
import br.com.emprestimo.repositories.LoanPaymentsRepository;
import br.com.emprestimo.repositories.LoanRepository;
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
        var payment = loanPaymentsRepository.findById(paymentId).orElseThrow(
                ()-> new PaymentFoundException("Payment " +paymentId+ " not found"));

        payment.setIsPayed(Boolean.TRUE);
        payment.setPaymentDay(LocalDate.now());
        loanPaymentsRepository.save(payment);
        log.info("Instalment {} payed", paymentId);
    }

}
