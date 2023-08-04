package br.com.emprestimo.repositories;

import br.com.emprestimo.domain.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, UUID> {

    List<LoanEntity> findAllByUserId(Long userId);

    @Query("SELECT le FROM LoanEntity le WHERE le.loanValue = :loanValue AND le.loanDateSigned = :dateSign AND le.loanDateDue = :dateDue AND le.isPayed = :isPayed")
    List<LoanEntity> findLoanByValueAndDates(
            @Param("loanValue") BigDecimal loanValue,
            @Param("dateSign") LocalDate dateSign,
            @Param("dateDue") LocalDate dateDue,
            @Param("isPayed") Boolean isPayed
    );
}
