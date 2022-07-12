package br.com.emprestimo.exception;

public class InvalidCPFException extends RuntimeException {

    public InvalidCPFException(String message) {
        super(message);
    }
}
