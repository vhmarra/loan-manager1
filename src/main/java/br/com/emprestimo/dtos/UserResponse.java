package br.com.emprestimo.dtos;

import br.com.emprestimo.domain.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserResponse {

    @JsonProperty(value = "user_cpf")
    private String cpf;

    @JsonProperty(value = "user_email")
    private String email;

    @JsonProperty(value = "user_name")
    private String name;

    @JsonProperty(value = "serasa_score")
    private int serasaScore;

    @JsonProperty(value = "user_loans")
    private List<LoanResponse> loanResponses = new ArrayList<>();

    public UserResponse(@NotNull UserEntity user) {
        this.cpf = user.getCpf();
        this.email = user.getEmail();
        this.name = user.getName();
        this.serasaScore = user.getSerasaScore();
    }
}
