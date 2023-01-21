package br.com.emprestimo.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SpringExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = InvalidCPFException.class)
    ResponseEntity<Object> InvalidCPFHandler(RuntimeException rtex, WebRequest request) {
        return handleExceptionInternal(rtex, rtex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    ResponseEntity<Object> UserNotFoundHandler(RuntimeException rtex, WebRequest request) {
        return handleExceptionInternal(rtex, rtex.getMessage(), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @ExceptionHandler(value = UserAlreadyHasUnpayLoansException.class)
    ResponseEntity<Object> UserAlreadyHasUnpayLoansExceptionHandler(RuntimeException rtex, WebRequest request) {
        return handleExceptionInternal(rtex, rtex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = PaymentNotFoundException.class)
    ResponseEntity<Object> PaymentNotFoundExceptionHandler(RuntimeException rtex, WebRequest request) {
        return handleExceptionInternal(rtex, rtex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = UnsupportedOperationException.class)
    ResponseEntity<Object> UnsupportedOperationExceptionHandler(RuntimeException rtex, WebRequest request) {
        return handleExceptionInternal(rtex, rtex.getMessage(), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
}
