package br.com.emprestimo.services;

import br.com.emprestimo.domain.UserFinancialAccountEntity;
import br.com.emprestimo.dtos.AddFundToAccountRequest;
import br.com.emprestimo.dtos.CreateAccountResponse;
import br.com.emprestimo.enums.AccountStatus;
import br.com.emprestimo.repositories.UserFinancialAccountRepository;
import br.com.emprestimo.utils.UserContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.EmptyStackException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserFinancialAccountService extends UserContextUtil {

    private final UserFinancialAccountRepository repository;

    @Transactional(rollbackOn = EmptyStackException.class)
    public CreateAccountResponse createAccount() {
        var userAccount = new UserFinancialAccountEntity();
        var user = getUser();
        var accountId = UUID.randomUUID();

        if (user.getIsUserActive()) {
            userAccount.setAccountId(accountId);
            userAccount.setAccountStatus(AccountStatus.OPEN);
            userAccount.setDateCreated(LocalDateTime.now());
            userAccount.setAvailableFunds(0.0);
            userAccount.setBlockedFunds(0.0);
            userAccount.setIsActive(Boolean.TRUE);
            userAccount.setUser(user);

            repository.save(userAccount);
            return new CreateAccountResponse(accountId.toString());
        } else {
            throw new IllegalStateException("User not active");
        }
    }

    @Transactional(rollbackOn = IllegalStateException.class)
    public void addFundToAccount(AddFundToAccountRequest dto) {
        var user = getUser();
        var account = repository.findByUserId(user.getId());
        var fundsBeforeAdd = account.getAvailableFunds();
        var blockedFundsBeforeAdd = account.getBlockedFunds();

        if (account.getIsActive()) {
            if (AccountStatus.OPEN.equals(account.getAccountStatus())) {
                log.info("Adding funds to account with id -> {} | Available funds before adding -> {}", account.getAccountId(), fundsBeforeAdd);
                account.setAvailableFunds(fundsBeforeAdd + dto.getAmount());
                account.setDateUpdated(LocalDateTime.now());
            } else if (AccountStatus.BLOCKED.equals(account.getAccountStatus())) {
                log.info("Adding funds to account with id -> {} | Available funds(blocked) before adding -> {}", account.getAccountId(), blockedFundsBeforeAdd);
                account.setBlockedFunds(blockedFundsBeforeAdd + dto.getAmount());
                account.setDateUpdated(LocalDateTime.now());
            } else {
                throw new IllegalStateException("Operation not allowed for this account");
            }
            repository.save(account);
        }
    }

    @Transactional(rollbackOn = Exception.class) //TODO generic exception change later
    public void changeAccountStatus(AccountStatus status) {
        var user = getUser();
        var account = repository.findByUserId(user.getId());

        account.setAccountStatus(status);
        log.info("Change status to -> {} for account with id -> {}", status, account.getAccountId());
        repository.save(account);
    }

}

