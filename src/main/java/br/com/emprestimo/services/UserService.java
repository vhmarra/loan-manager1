package br.com.emprestimo.services;

import br.com.emprestimo.kafka.producer.CreateUserKafkaSender;
import br.com.emprestimo.domain.UserEntity;
import br.com.emprestimo.dtos.LoanResponse;
import br.com.emprestimo.dtos.UserResponse;
import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.exception.UserNotFoundException;
import br.com.emprestimo.repositories.LoanRepository;
import br.com.emprestimo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

import static br.com.emprestimo.utils.CpfValidation.validateCpf;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final CreateUserKafkaSender createUserKafkaSender;

    @Transactional
    public void signUpUser(UserSignUpRequest request) {
        validateCpf(request.getCpf());
        var userToSave = new UserEntity(request);
        userRepository.save(userToSave);
        log.info("User saved -> {}", userToSave.getName());

    }

    @Transactional
    public void updateUserStatus(String userCpf, String sBoolean) {
        var user = userRepository.findUserByCpf(userCpf);
        if (user.isPresent()) {
            user.get().setIsUserActive(Boolean.valueOf(sBoolean));
            var userUpdated = userRepository.save(user.get());
            log.info("status updated -> {} {}", userUpdated.getCpf(), userUpdated.getIsUserActive());
        } else {
            throw new UserNotFoundException("user not found");
        }
    }

    public UserResponse getUserData(String userCpf) {
        var user = userRepository.findUserByCpf(userCpf);
        if (user.isPresent()) {
            var userResponse = new UserResponse(user.get());
            var loans = loanRepository.findAllByUserId(user.get().getId());
            var loansResponse = new ArrayList<LoanResponse>();
            loans.forEach(loanEntity -> {
                var loanResponse = new LoanResponse(loanEntity);
                loansResponse.add(loanResponse);
            });
            userResponse.setLoanResponses(loansResponse);
            return userResponse;
        }
        return new UserResponse();
    }

    public void sendUserToQueue(UserSignUpRequest request) {
        createUserKafkaSender.sendMessage(request);
    }
}
