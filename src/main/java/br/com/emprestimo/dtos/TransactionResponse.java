package br.com.emprestimo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionResponse {

    @JsonProperty(value = "transaction-id")
    private UUID transactionId;

    @JsonProperty(value = "transaction-value")
    private Double transactionValue;

    @JsonProperty(value = "account-in")
    private UUID accountIn;

    @JsonProperty(value = "account-out")
    private UUID accountOut;

    private LocalDateTime date;

}
