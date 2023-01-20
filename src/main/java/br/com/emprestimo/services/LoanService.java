package br.com.emprestimo.services;

import br.com.emprestimo.domain.LoanEntity;
import br.com.emprestimo.dtos.LoanRequest;
import br.com.emprestimo.exception.UserAlreadyHasUnpayLoansException;
import br.com.emprestimo.repositories.LoanRepository;
import br.com.emprestimo.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class LoanService {

    private final LoanRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public void requestLoan(LoanRequest request) {
        loanIsEligible(request.getLoanValue(),request.getLoanDateSigned(),request.getLoanDateDue());
        var user = userRepository.findUserByCpf(request.getUserCpf());
        if (user.isPresent() && user.get().getIsUserActive()) {
            var loan = new LoanEntity(request);
            loan.setUser(user.get());
            log.info("Saving loan {}",loan.getLoanId());
            repository.save(loan);
            log.info("Loan saved {}",loan.getLoanId());
        } else {
            throw new UnsupportedOperationException("error while save loan");
        }
    }
    @Transactional
    public void updateLoanStatus(UUID loanId, String loanStatus) {
        var loan = repository.findById(loanId);
        if (loan.isPresent() && loan.get().getUser().getIsUserActive()) {
            loan.get().setIsApproved(Boolean.valueOf(loanStatus));
            repository.save(loan.get());
        } else {
            throw new UnsupportedOperationException("loan not found or user is not activated");
        }
    }

    public void payLoan(UUID loanId) {
        var loan = repository.findById(loanId);
        if (loan.isPresent() && loan.get().getIsApproved() && loan.get().getUser().getIsUserActive()) {
            loan.get().setIsPayed(Boolean.TRUE);
            repository.save(loan.get());
        } else {
            throw new UnsupportedOperationException("error to pay loan");
        }
    }

    private void loanIsEligible(BigDecimal loanValue, String dateSign, String dateDue) {
        var loans = repository.findLoanByValueAndDates(loanValue,LocalDate.parse(dateSign),LocalDate.parse(dateDue),false);
        if (!loans.isEmpty()) {
            throw new UserAlreadyHasUnpayLoansException("User already has unpay loans");
        }
    }

}
