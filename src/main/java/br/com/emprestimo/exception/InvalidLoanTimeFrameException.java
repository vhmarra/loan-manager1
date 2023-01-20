package br.com.emprestimo.exception;

public class InvalidLoanTimeFrameException extends RuntimeException {

    public InvalidLoanTimeFrameException(String message) {
        super(message);
    }
}
