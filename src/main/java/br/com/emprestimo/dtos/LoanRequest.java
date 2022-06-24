package br.com.emprestimo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {

    @JsonProperty(value = "loan-value")
    private BigDecimal loanValue;

    @JsonProperty(value = "loan-date-signed")
    private String loanDateSigned;

    @JsonProperty(value = "loan-date-due")
    private String loanDateDue;

    @JsonProperty(value = "loan-user-cpf")
    private String userCpf;

}
