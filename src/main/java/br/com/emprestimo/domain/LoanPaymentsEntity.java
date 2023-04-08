package br.com.emprestimo.domain;

import br.com.emprestimo.dtos.LoanRequest;
import br.com.emprestimo.enums.LoanTimeFrame;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "LOAN_PAYMENTS_TB")
@NoArgsConstructor
@Data
public class LoanPaymentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_id")
    @Type(type = "uuid-char")
    private UUID paymentId;

    @Column(name = "loan_value")
    private Double paymentValue;

    @Column(name = "loan_payment_supposed_day")
    private LocalDate paymentSupposedPayDay;

    @Column(name = "loan_date_payed")
    private LocalDate paymentDay;

    @Column(name = "is_payed")
    private Boolean isPayed = false;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "loan_id")
    private LoanEntity loan;

}
