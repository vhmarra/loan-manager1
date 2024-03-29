package br.com.emprestimo.repositories;

import br.com.emprestimo.domain.AccessToken;
import br.com.emprestimo.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    Optional<AccessToken> findByToken(String token);

    Optional<AccessToken> findByUser(UserEntity user);
}
