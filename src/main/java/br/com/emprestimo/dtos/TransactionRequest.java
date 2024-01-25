package br.com.emprestimo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

    @JsonProperty(value = "account-in")
    private Long accountIn;

    @JsonProperty(value = "value")
    private Double value;

}
