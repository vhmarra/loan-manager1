package br.com.emprestimo.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.emprestimo.domain.UserEntity;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class PasswordValidation {

    public static void ValidatePassword(UserEntity user, String passwordToValidate) {
        if (!BCrypt.verifyer().verify(passwordToValidate.getBytes(), user.getPassword().getBytes()).verified) {
            throw new SecurityException("AUTH ERROR");
        }
    }

}
