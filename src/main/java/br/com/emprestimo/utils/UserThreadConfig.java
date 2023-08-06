package br.com.emprestimo.utils;

import br.com.emprestimo.domain.AccessToken;

public final class UserThreadConfig {

    private static ThreadLocal<AccessToken> tClocal = new ThreadLocal<>();

    private UserThreadConfig() {
        super();
    }

    public static void removeToken() {
        tClocal.remove();
    }

    public static ThreadLocal<AccessToken> getToken() {
        return tClocal;
    }

    public static void setToken(AccessToken token) {
        tClocal.set(token);
    }
}
