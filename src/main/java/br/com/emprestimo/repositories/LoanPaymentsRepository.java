package br.com.emprestimo.repositories;

import br.com.emprestimo.domain.LoanEntity;
import br.com.emprestimo.domain.LoanPaymentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoanPaymentsRepository extends JpaRepository<LoanPaymentsEntity, UUID> {

    Optional<LoanPaymentsEntity> findByPaymentIdAndIsPayed(UUID uuid,Boolean isPayed);

    List<LoanPaymentsEntity> findAllByLoan(LoanEntity loan);
}
