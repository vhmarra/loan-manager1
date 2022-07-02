package br.com.emprestimo.utils;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CpfValidation {
    public static void validateCpf(final String cpf) {
        var validator = new CPFValidator();
        validator.assertValid(cpf);
    }

}
