package br.com.emprestimo.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class InterestService {

    private final Environment env;

    public Double getInterestValue() {
        var interest = Double.valueOf(Objects.requireNonNull(env.getProperty("loan.interest.value")));
        log.info("Getting interest rate of = {}", interest);
        return interest;
    }
}
