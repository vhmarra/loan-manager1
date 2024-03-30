package br.com.emprestimo.domain;

import br.com.emprestimo.enums.TransactionStatus;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TRANSACTION_TB")
@Data
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id", columnDefinition = "uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID transactionId;

    @Column(name = "dt_created")
    private LocalDateTime dateCreated;

    @Column(name = "dt_completed")
    private LocalDateTime dateCompleted;

    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(name = "transaction_value")
    private Double transactionValue;

    @Column(name = "user_in")
    private Long userInId;

    @Column(name = "user_out")
    private Long userOutId;

}
