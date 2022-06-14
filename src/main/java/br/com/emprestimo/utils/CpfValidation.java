package br.com.emprestimo.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CpfValidation {


    //TODO basic validation must implement the correct algorithm for cpf validation
    public static void validateCpf(final String cpf) {
        if (cpf.length() != 11) {
            throw new UnsupportedOperationException("Cpf must be 11 digits");
        }

    }

}
