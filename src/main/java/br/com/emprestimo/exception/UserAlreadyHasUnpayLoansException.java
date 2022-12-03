package br.com.emprestimo.exception;

public class UserAlreadyHasUnpayLoansException extends RuntimeException {

    public UserAlreadyHasUnpayLoansException(String message) {
        super(message);
    }
}
