package br.com.emprestimo.repositories;

import br.com.emprestimo.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserByCpf(@Param("cpf") String cpf);

    @Query(value = "SELECT * FROM user_tb where user_cpf = :credential or user_email = :credential", nativeQuery = true)
    Optional<UserEntity> findUserByEmailOrCpf(@Param(value = "credential") String credential);

}
