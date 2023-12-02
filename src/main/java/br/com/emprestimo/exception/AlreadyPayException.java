package br.com.emprestimo.exception;

public class AlreadyPayException extends RuntimeException {

    public AlreadyPayException(String message) {
        super(message);
    }
}
