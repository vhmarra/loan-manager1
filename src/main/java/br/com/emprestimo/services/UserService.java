package br.com.emprestimo.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.emprestimo.domain.UserEntity;
import br.com.emprestimo.dtos.LoanPaymentResponse;
import br.com.emprestimo.dtos.LoanResponse;
import br.com.emprestimo.dtos.UserResponse;
import br.com.emprestimo.dtos.UserSignUpRequest;
import br.com.emprestimo.enums.Topics;
import br.com.emprestimo.exception.UserNotFoundException;
import br.com.emprestimo.kafka.producer.KafkaSender;
import br.com.emprestimo.repositories.LoanPaymentsRepository;
import br.com.emprestimo.repositories.LoanRepository;
import br.com.emprestimo.repositories.UserRepository;
import br.com.emprestimo.utils.PasswordValidation;
import br.com.emprestimo.utils.UserContextUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static br.com.emprestimo.utils.CpfValidation.validateCpf;
import static br.com.emprestimo.utils.PasswordValidation.ValidatePassword;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService extends UserContextUtil {

    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final LoanPaymentsRepository loanPaymentsRepository;
    private final KafkaSender sender;
    private final AccessTokenService accessTokenService;

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

    public UserResponse getUserData() {
        var user = getUser();
        if (Objects.nonNull(user)) {
            var userResponse = new UserResponse(user);
            var loans = loanRepository.findAllByUserId(user.getId());
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

    @Transactional(rollbackOn = {SQLException.class, SecurityException.class})
    public String authenticate(final String credential, final String pwd) {
        var user = userRepository.findUserByEmailOrCpf(credential).orElseThrow(() -> new UserNotFoundException("User not found"));

        ValidatePassword(user, pwd);

        var token = accessTokenService.createToken(user);
        userRepository.save(user);
        setAccessToken(token);

        return token.getToken();
    }

    public void sendUserToQueue(UserSignUpRequest request) {
        var parser = new Gson().toJson(request);
        sender.sendMessage(parser, Topics.CREATE_USER_TOPIC.getTopicName());
    }
}
