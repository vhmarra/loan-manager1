package br.com.emprestimo.dtos;

import br.com.emprestimo.domain.LoanPaymentsEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class LoanPaymentResponse {

    @JsonProperty(value = "loan-payment-id")
    private UUID paymentId;

    @JsonProperty(value = "payment-value")
    private Double loanValue;

    @JsonProperty(value = "loan-payment-supposed-day")
    private LocalDate paymentSupposedPayDay;

    @JsonProperty(value = "loan-date-payed")
    private LocalDate paymentDay;

    @JsonProperty(value = "is-payed")
    private Boolean isPayed;

    public LoanPaymentResponse(LoanPaymentsEntity payment) {
        this.paymentId = payment.getPaymentId();
        this.loanValue = payment.getPaymentValue();
        this.paymentSupposedPayDay = payment.getPaymentSupposedPayDay();
        this.paymentDay = payment.getPaymentDay();
        this.isPayed = payment.getIsPayed();
    }
}
