package br.com.emprestimo.controllers;

import br.com.emprestimo.dtos.TransactionRequest;
import br.com.emprestimo.services.TransactionalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionalService service;

    @PostMapping("/create")
    public ResponseEntity<?> createTransactionSync(@RequestBody TransactionRequest request,
                                                   @RequestHeader(name = "auth-token") String authCode) {
        var response = service.transfer(request);
        return ResponseEntity.ok(response);
    }


}