package br.com.emprestimo.dtos;

import lombok.Data;

@Data
public class LogRequestDto {

    private String message;
    private String request;
    private String response;
}
