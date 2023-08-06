package br.com.emprestimo.services;

import br.com.emprestimo.domain.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AccessTokenService {

    public boolean validateToken(AccessToken token) {
        return LocalDateTime.now().isAfter(token.getDateValid());
    }
}
