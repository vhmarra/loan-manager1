package br.com.emprestimo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountStatus {
    OPEN("OPEN"),
    BLOCKED("BLOCKED"),
    DEACTIVATED("DEACTIVATED"),
    CLOSED("CLOSED");

    private final String code;
}
