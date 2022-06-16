package br.com.emprestimo.dtos;

import br.com.emprestimo.domain.UserEntity;
import br.com.emprestimo.enums.LoanTimeFrame;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static javax.persistence.EnumType.STRING;

@Data
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
