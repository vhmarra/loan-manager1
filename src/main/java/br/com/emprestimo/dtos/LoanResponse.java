package br.com.emprestimo.dtos;

import br.com.emprestimo.domain.LoanEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanResponse {
    @JsonProperty(value = "loan-value")
    private BigDecimal loanValue;

    @JsonProperty(value = "is-loan-approved")
    private Boolean isApproved;

    @JsonProperty(value = "loan-time-frame")
    private String loanTimeFrame;

    @JsonProperty(value = "loan-date-signed")
    private String loanDateSigned;

    @JsonProperty(value = "loan-date-due")
    private String loanDateDue;

    public LoanResponse(LoanEntity loan) {
        this.loanValue = loan.getLoanValue();
        this.isApproved = loan.getIsApproved();
        this.loanTimeFrame = loan.getLoanTimeFrame().toString();
        this.loanDateSigned = loan.getLoanDateSigned().toString();
        this.loanDateDue = loan.getLoanDateDue().toString();
    }
}
