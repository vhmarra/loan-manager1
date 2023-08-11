package br.com.emprestimo.services;

import br.com.emprestimo.domain.AccessToken;
import br.com.emprestimo.domain.UserEntity;
import br.com.emprestimo.repositories.AccessTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccessTokenService {

    private final AccessTokenRepository repository;

    public boolean validateToken(AccessToken token) {
        return LocalDateTime.now().isAfter(token.getDateValid());
    }

    @Transactional(rollbackOn = Exception.class)
    public AccessToken createToken(UserEntity user) {
        var userToken = repository.findByUser(user).orElse(null);
        if (null == userToken || this.validateToken(userToken)) {
            if (null != userToken) {
                repository.delete(userToken);
            }
            var accessToken = new AccessToken();
            var now = LocalDateTime.now();
            var expiredAt = LocalDateTime.now().plusDays(1L);
            accessToken.setToken(UUID.randomUUID().toString());
            accessToken.setUser(user);
            accessToken.setIsActive(true);
            accessToken.setDateCreated(now);
            accessToken.setDateValid(expiredAt);
            user.setIsUserActive(true);
            repository.save(accessToken);
            return accessToken;
        } else return userToken;
    }
}
