package br.com.emprestimo.controllers;

import br.com.emprestimo.dtos.LoanRequest;
import br.com.emprestimo.services.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/loan")
@AllArgsConstructor
public class LoanController {

    private final LoanService service;

    @PostMapping
    public ResponseEntity<?> requestLoan(@RequestBody LoanRequest request) {
        service.requestLoan(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "update")
    public ResponseEntity<?> updateLoanStatus(@RequestHeader(value = "loan-id") UUID loanId, String loanStatus) {
        service.updateLoanStatus(loanId, loanStatus);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "pay")
    public ResponseEntity<?> payLoan(@RequestHeader(value = "loan-id") UUID loanId) {
        service.payLoan(loanId);
        return ResponseEntity.ok().build();
    }

}
