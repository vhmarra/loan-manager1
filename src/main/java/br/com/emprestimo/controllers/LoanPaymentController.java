package br.com.emprestimo.controllers;

import br.com.emprestimo.services.LoanPaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/loan-payment")
@AllArgsConstructor
public class LoanPaymentController {

    private final LoanPaymentService service;

    @PatchMapping(value = "pay-instalment")
    public ResponseEntity<?> payInstalment(@RequestHeader(value = "loan-payment-id") UUID loanId, @RequestHeader(name = "auth-token") String authCode) {
        service.payInstalment(loanId);
        return ResponseEntity.ok().build();
    }

}
