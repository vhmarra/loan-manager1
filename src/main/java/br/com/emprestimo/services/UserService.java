package br.com.emprestimo.services;

import br.com.emprestimo.domain.UserEntity;
import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.repositories.UserRepository;
import br.com.emprestimo.utils.CpfValidation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static br.com.emprestimo.utils.CpfValidation.validateCpf;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(rollbackOn = UnsupportedOperationException.class)
    public void signUpUser(UserSignUpRequest request) {
        validateCpf(request.getCpf());
        var userToSave = new UserEntity(request);
        userRepository.save(userToSave);
        log.info("User saved -> {}",userToSave.getName());
    }

}
