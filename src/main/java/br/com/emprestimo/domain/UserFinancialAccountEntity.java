package br.com.emprestimo.domain;

import br.com.emprestimo.enums.AccountStatus;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "USER_FINANCIAL_ACCOUNT_TB")
@Data
public class UserFinancialAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id", columnDefinition = "uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID accountId;

    @Column(name = "available_funds")
    private Double availableFunds;

    @Column(name = "blocked_funds")
    private Double blockedFunds;

    @Column(name = "active")
    private Boolean isActive;

    @Column(name = "account_status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(name = "dt_created")
    private LocalDateTime dateCreated;

    @Column(name = "dt_updated")
    private LocalDateTime dateUpdated;

    @OneToOne
    private UserEntity user;

}
