package br.com.emprestimo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class UserSignUpRequest {

    @JsonProperty(value = "user_cpf")
    private String cpf;

    @JsonProperty(value = "user_email")
    @Email(message = "Email out of format eg: email@email.com")
    private String email;

    @JsonProperty(value = "user_pwd")
    private String password;

    @JsonProperty(value = "user_name")
    private String name;

    @JsonProperty(value = "serasa_score")
    @Min(value = 0, message = "min serasa score must be grater than 0")
    @Max(value = 1000, message = "max serasa score must be lower than 1000")
    private int serasaScore;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserSignUpRequest{");
        sb.append("cpf='").append(cpf).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", serasaScore=").append(serasaScore);
        sb.append('}');
        return sb.toString();
    }
}
