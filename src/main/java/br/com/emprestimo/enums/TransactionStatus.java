package br.com.emprestimo.enums;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    CREATED,
    COMPLETED,
    FAILED,
    REVERTED,
    REVERTED_REQUEST
}
