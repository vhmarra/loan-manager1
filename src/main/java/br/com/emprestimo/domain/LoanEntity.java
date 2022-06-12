package br.com.emprestimo.domain;

import br.com.emprestimo.enums.LoanTimeFrame;
import lombok.Data;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "LOAN_ENTITY")
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

}
