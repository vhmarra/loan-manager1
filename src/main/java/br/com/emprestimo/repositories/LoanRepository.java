package br.com.emprestimo.repositories;

import br.com.emprestimo.domain.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, UUID> {

    List<LoanEntity> findAllByUserId(Long userId);
}
