package br.com.emprestimo.domain;

import br.com.emprestimo.dtos.LoanRequest;
import br.com.emprestimo.enums.LoanTimeFrame;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "LOAN_ENTITY")
@NoArgsConstructor
@Data
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "loan_id")
    @Type(type = "uuid-char")
    private UUID loanId;

    @Column(name = "loan_value")
    private BigDecimal loanValue;

    @Column(name = "approved")
    private Boolean isApproved;

    @Column(name = "loan_time_frame")
    @Enumerated(value = STRING)
    private LoanTimeFrame loanTimeFrame;

    @Column(name = "loan_date_signed")
    private LocalDate loanDateSigned;

    @Column(name = "loan_date_due")
    private LocalDate loanDateDue;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public LoanEntity(LoanRequest request) {
        this.loanValue = request.getLoanValue();
        this.loanDateSigned = LocalDate.parse(request.getLoanDateSigned(), DateTimeFormatter.ISO_LOCAL_DATE);
        this.loanDateDue = LocalDate.parse(request.getLoanDateDue(), DateTimeFormatter.ISO_LOCAL_DATE);
        this.loanTimeFrame = validateTimeFrame(LocalDate.parse(request.getLoanDateSigned(), DateTimeFormatter.ISO_LOCAL_DATE)
                ,LocalDate.parse(request.getLoanDateDue(), DateTimeFormatter.ISO_LOCAL_DATE));
        this.isApproved = Boolean.FALSE;

    }

    private LoanTimeFrame validateTimeFrame(LocalDate signed, LocalDate due) {
        if (signed == due) return LoanTimeFrame.D_ZERO;
        else return LoanTimeFrame.D_THIRTY;
    }
}
