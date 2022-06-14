package br.com.emprestimo.controllers;

import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<?> userSignUp(@RequestBody UserSignUpRequest request) {
        service.signUpUser(request);
        return ResponseEntity.ok().build();
    }

}
