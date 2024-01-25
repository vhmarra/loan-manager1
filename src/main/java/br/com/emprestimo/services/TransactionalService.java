package br.com.emprestimo.services;

import br.com.emprestimo.domain.TransactionEntity;
import br.com.emprestimo.domain.UserEntity;
import br.com.emprestimo.dtos.TransactionRequest;
import br.com.emprestimo.dtos.TransactionResponse;
import br.com.emprestimo.enums.AccountStatus;
import br.com.emprestimo.enums.TransactionStatus;
import br.com.emprestimo.exception.PaymentNotFoundException;
import br.com.emprestimo.exception.UserNotFoundException;
import br.com.emprestimo.repositories.TransactionRepository;
import br.com.emprestimo.repositories.UserFinancialAccountRepository;
import br.com.emprestimo.repositories.UserRepository;
import br.com.emprestimo.utils.UserContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionalService extends UserContextUtil {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final UserFinancialAccountService userFinancialAccountService;
    private final UserFinancialAccountRepository accountRepository;

    @Transactional(rollbackOn = {Exception.class, PaymentNotFoundException.class})
    public TransactionResponse transfer(TransactionRequest request) {
        var userIn = userRepository.findById(request.getAccountIn()).orElseThrow(() -> new UserNotFoundException("User not found"));
        var userOut = getUser();
        var accounts = accountRepository.findByUserIdIn(List.of(userIn.getId(), userOut.getId()));
        if (accounts.size() < 2) {
            throw new UserNotFoundException("User not found");
        }

        var accountIn = accounts.get(0);
        var accountOut = accounts.get(1);

        var isAccountsValid = validateUserForTransaction(userIn) && validateUserForTransaction(userOut);
        if (!isAccountsValid) {
            throw new PaymentNotFoundException("Accounts invalid for transaction");
        }
        var accountOutHasFunds = accountOut.getAvailableFunds() - request.getValue() >= 0.0;
        if (!accountOutHasFunds) {
            throw new PaymentNotFoundException("No funds for this transaction");
        }

        accountIn.setAvailableFunds(accountIn.getAvailableFunds() + request.getValue());
        accountOut.setAvailableFunds(accountOut.getAvailableFunds() - request.getValue());

        var transaction = new TransactionEntity();

        var response = new TransactionResponse();
        try {
            transaction.setTransactionValue(request.getValue());
            transaction.setTransactionStatus(TransactionStatus.CREATED);
            transaction.setUserInId(userIn.getId());
            transaction.setUserOutId(userOut.getId());
            transaction.setDateCreated(LocalDateTime.now());
            transactionRepository.save(transaction);

            accountRepository.save(accountIn);
            accountRepository.save(accountOut);
            transaction.setTransactionStatus(TransactionStatus.COMPLETED);
            transaction.setDateCompleted(LocalDateTime.now());
            transactionRepository.save(transaction);
            response.setTransactionId(transaction.getTransactionId());
            response.setDate(LocalDateTime.now());
            response.setAccountIn(accountIn.getAccountId());
            response.setAccountOut(accountOut.getAccountId());
            response.setTransactionValue(transaction.getTransactionValue());
        } catch (Exception e) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transaction.setDateCompleted(LocalDateTime.now());
            transactionRepository.save(transaction);
        }

        return response;
    }

    private Boolean validateUserForTransaction(UserEntity user) {
        var userAccount = userFinancialAccountService.getAccountByUser(user);
        if (Objects.isNull(userAccount)) {
            return false;
        }

        return user.getIsUserActive() && userAccount.getIsActive() && userAccount.getAccountStatus().equals(AccountStatus.OPEN);
    }


}

