package br.com.emprestimo.repositories;

import br.com.emprestimo.domain.UserFinancialAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserFinancialAccountRepository extends JpaRepository<UserFinancialAccountEntity, UUID> {

    UserFinancialAccountEntity findByUserId(Long userId);

}
