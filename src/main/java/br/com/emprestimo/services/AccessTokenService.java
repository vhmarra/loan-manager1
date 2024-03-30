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


    @Transactional(rollbackOn = Exception.class)
    public AccessToken createToken(UserEntity user) {
        var userToken = repository.findByUser(user).orElse(null);
        if (null == userToken || this.validateToken(userToken)) {
            if (null != userToken) {
                repository.delete(userToken);
            }
            var accessToken = generateToken(user);
            repository.save(accessToken);
            return accessToken;
        } else return userToken;
    }

    private AccessToken generateToken(UserEntity user) {
        var accessToken = new AccessToken();
        var now = LocalDateTime.now();
        var expiredAt = LocalDateTime.now().plusDays(1L);
        var tokenUuid = UUID.randomUUID().toString();
        accessToken.setToken(tokenUuid);
        accessToken.setUser(user);
        accessToken.setIsActive(true);
        accessToken.setDateCreated(now);
        accessToken.setDateValid(expiredAt);
        user.setIsUserActive(true);

        //TODO remove later dont log tokens
        log.info("Token for user with id -> {} created with value -> {}", user.getId(), tokenUuid);
        return accessToken;
    }

    public boolean validateToken(AccessToken token) {
        return LocalDateTime.now().isAfter(token.getDateValid());
    }
}
