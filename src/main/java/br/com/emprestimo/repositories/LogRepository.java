package br.com.emprestimo.repositories;

import br.com.emprestimo.domain.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, UUID> {
}
