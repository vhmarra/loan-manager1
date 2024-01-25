package br.com.emprestimo.controllers;

import br.com.emprestimo.dtos.AddFundToAccountRequest;
import br.com.emprestimo.dtos.CreateAccountResponse;
import br.com.emprestimo.dtos.UserResponse;
import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.services.UserFinancialAccountService;
import br.com.emprestimo.services.UserService;
import br.com.emprestimo.utils.CpfValidation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService service;
    private final UserFinancialAccountService accountService;


    @PostMapping
    public ResponseEntity<?> userSignUp(@RequestBody UserSignUpRequest request) {
        service.sendUserToQueue(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping
    public ResponseEntity<?> userUpdate(@RequestHeader(name = "user-cpf") String userCpf,
                                        @RequestHeader(name = "boolean-value") String bolValue) {
        service.updateUserStatus(userCpf, bolValue);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserData(@RequestHeader(name = "auth-token") String authCode) {
        return ResponseEntity.of(Optional.of(service.getUserData()));
    }

    @PostMapping(value = "/auth")
    public ResponseEntity<?> authUser(@RequestHeader(name = "credential") String credential, @RequestHeader(name = "pwd") String pwd) {
        return ResponseEntity.ok(service.authenticate(credential, pwd));
    }

    @PostMapping(value = "/financial-account")
    public ResponseEntity<CreateAccountResponse> createFinancialAccount(@RequestHeader(name = "auth-token") String authCode) {
        var response = accountService.createAccount();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "account/add-funds")
    public ResponseEntity<?> addFundsToAccount(@RequestHeader(name = "auth-token") String authCode, @RequestBody AddFundToAccountRequest request) {
        accountService.addFundToAccount(request);
        return ResponseEntity.ok().build();
    }
}
