package br.com.emprestimo.controllers;

import br.com.emprestimo.dtos.LoanRequest;
import br.com.emprestimo.services.LoanPaymentService;
import br.com.emprestimo.services.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/loan-payment")
@AllArgsConstructor
public class LoanPaymentController {

    private final LoanPaymentService service;

    @PatchMapping(value = "pay-instalment")
    public ResponseEntity<?> payInstalment(@RequestHeader(value = "loan-payment-id") UUID loanId) {
        service.payInstalment(loanId);
        return ResponseEntity.ok().build();
    }

}
