package br.com.emprestimo.controllers;

import br.com.emprestimo.dtos.LoanRequest;
import br.com.emprestimo.services.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
