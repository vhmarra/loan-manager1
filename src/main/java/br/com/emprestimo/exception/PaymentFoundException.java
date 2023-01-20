package br.com.emprestimo.exception;

public class PaymentFoundException extends RuntimeException {

    public PaymentFoundException(String message) {
        super(message);
    }
}
