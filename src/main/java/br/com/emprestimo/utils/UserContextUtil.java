package br.com.emprestimo.utils;

import br.com.emprestimo.domain.AccessToken;
import br.com.emprestimo.domain.UserEntity;
import org.springframework.stereotype.Service;

import java.util.EmptyStackException;

@Service
public class UserContextUtil {

    public UserEntity getUser() {
        var user = UserThreadConfig.getToken().get().getUser();
        if (null == user) {
            throw new EmptyStackException();
        }
        return UserThreadConfig.getToken().get().getUser();
    }

    public AccessToken getAccessToken() {
        return UserThreadConfig.getToken().get();
    }

    public void setAccessToken(AccessToken token) {
        UserThreadConfig.setToken(token);
    }
}
