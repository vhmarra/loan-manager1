package br.com.emprestimo.repositories;

import br.com.emprestimo.domain.UserFinancialAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserFinancialAccountRepository extends JpaRepository<UserFinancialAccountEntity, UUID> {
    UserFinancialAccountEntity findByUserId(Long userId);
    List<UserFinancialAccountEntity> findByUserIdIn(List<Long> ids);

}
