package br.com.emprestimo.utils;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.emprestimo.exception.InvalidCPFException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class CpfValidation {
    public static void validateCpf(final String cpf) {
        var validator = new CPFValidator();
        try {
            validator.assertValid(cpf);
        } catch (InvalidStateException ex) {
            log.error("CPF {} is invalid",cpf);
            throw new InvalidCPFException("CPF IS INVALID");
        }
    }

}
