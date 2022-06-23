package br.com.emprestimo.controllers;

import br.com.emprestimo.dtos.UserResponse;
import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.services.UserService;
import br.com.emprestimo.utils.CpfValidation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<?> userSignUp(@RequestBody UserSignUpRequest request) {
        service.signUpUser(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<?> userUpdate(@RequestHeader(name = "user-cpf") String userCpf,
                                        @RequestHeader(name = "boolean-value") String bolValue) {
        CpfValidation.validateCpf(userCpf);
        service.updateUserStatus(userCpf,bolValue);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserData(@RequestHeader(name = "user-cpf") String userCpf) {
        CpfValidation.validateCpf(userCpf);
        return ResponseEntity.of(Optional.of(service.getUserData(userCpf)));
    }

}
