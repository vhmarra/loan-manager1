package br.com.emprestimo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserSignUpRequest {

    @JsonProperty(value = "user_cpf")
    private String cpf;
    @JsonProperty(value = "user_email")
    private String email;
    @JsonProperty(value = "user_name")
    private String name;

}
