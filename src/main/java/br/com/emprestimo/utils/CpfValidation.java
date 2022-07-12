package br.com.emprestimo.utils;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.emprestimo.exception.InvalidCPFException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CpfValidation {
    public static void validateCpf(final String cpf) {
        var validator = new CPFValidator();
        try {
            validator.assertValid(cpf);
        } catch (InvalidStateException ex) {
            throw new InvalidCPFException("CPF IS INVALID");
        }
    }

}
