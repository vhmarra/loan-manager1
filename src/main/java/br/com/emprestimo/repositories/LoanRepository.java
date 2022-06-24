package br.com.emprestimo.repositories;

import br.com.emprestimo.domain.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, UUID> {

    List<LoanEntity> findAllByUserId(Long userId);
    @Query(value = "SELECT COUNT(1) FROM loan_entity le WHERE le.user_id = :userId and le.approved = false",nativeQuery = true)
    int findNotApprovedFromUser(Long userId);
}
