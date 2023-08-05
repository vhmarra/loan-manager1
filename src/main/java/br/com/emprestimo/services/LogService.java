package br.com.emprestimo.services;

import br.com.emprestimo.domain.LogEntity;
import br.com.emprestimo.dtos.LogRequestDto;
import br.com.emprestimo.repositories.LogRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Data
@Slf4j
public class LogService {

    private final LogRepository repository;

    public void handleLogMessage(LogRequestDto request) {
        var log = new LogEntity(request);
        log.setLogDate(LocalDateTime.now());
        repository.save(log);
    }


}
