package br.com.emprestimo.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.emprestimo.domain.AccessToken;
import br.com.emprestimo.domain.UserEntity;
import br.com.emprestimo.dtos.LoanPaymentResponse;
import br.com.emprestimo.dtos.LoanResponse;
import br.com.emprestimo.dtos.UserResponse;
import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.exception.UserNotFoundException;
import br.com.emprestimo.kafka.producer.CreateUserKafkaSender;
import br.com.emprestimo.repositories.AccessTokenRepository;
import br.com.emprestimo.repositories.LoanPaymentsRepository;
import br.com.emprestimo.repositories.LoanRepository;
import br.com.emprestimo.repositories.UserRepository;
import br.com.emprestimo.utils.UserContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import static br.com.emprestimo.utils.CpfValidation.validateCpf;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService extends UserContextUtil {

    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final LoanPaymentsRepository loanPaymentsRepository;
    private final CreateUserKafkaSender createUserKafkaSender;
    private final AccessTokenRepository accessTokenRepository;

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
            var loanPaymentsResponse = new ArrayList<LoanPaymentResponse>();
            loans.forEach(loanEntity -> {
                var loanResponse = new LoanResponse(loanEntity);
                var loanPaymentsEntity = loanPaymentsRepository.findAllByLoan(loanEntity);
                loanPaymentsEntity.forEach(it -> {
                    var loanPaymentResponse = new LoanPaymentResponse(it);
                    loanPaymentsResponse.add(loanPaymentResponse);
                });
                loansResponse.add(loanResponse);
            });
            userResponse.setLoanResponses(loansResponse);
            userResponse.setLoanPaymentResponses(loanPaymentsResponse);
            return userResponse;
        }
        return new UserResponse();
    }

    @Transactional(rollbackOn = SQLException.class)
    public String authenticate(final String email, final String pwd) {
        var user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!BCrypt.verifyer().verify(pwd.getBytes(), user.getPassword().getBytes()).verified) {
            throw new RuntimeException("AUTH ERROR");
        }

        var accessToken = new AccessToken();
        accessToken.setToken(UUID.randomUUID().toString());
        accessToken.setUser(user);
        accessToken.setIsActive(true);
        user.setIsUserActive(true);
        accessTokenRepository.save(accessToken);
        userRepository.save(user);
        setAccessToken(accessToken);

        return accessToken.getToken();
    }

    public void sendUserToQueue(UserSignUpRequest request) {
        createUserKafkaSender.sendMessage(request);
    }
}
