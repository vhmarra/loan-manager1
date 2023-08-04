package br.com.emprestimo.kafka.consumer;

import br.com.emprestimo.services.LoanPaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CreatePaymentsKafkaConsumer {

    private final LoanPaymentService service;

    @KafkaListener(topics = "create.payments.topic", groupId = "group-id")
    void createUser(String loanId) {
        log.info("Creating loan payments from loan -> {}", loanId);
        service.createLoanPayments(loanId);
    }

}
