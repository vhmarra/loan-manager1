package br.com.emprestimo.domain;

import br.com.emprestimo.dtos.LogRequestDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "LOG_TB")
@NoArgsConstructor
@Data
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "log_id", columnDefinition = "uuid")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID loanId;

    @Column(name = "log_message")
    private String message;

    @Column(name = "request")
    private String request;

    @Column(name = "response")
    private String response;

    @Column(name = "log_date")
    private LocalDateTime logDate;

    public LogEntity(LogRequestDto dto) {
        this.message = dto.getMessage();
        this.request = dto.getRequest();
        this.response = dto.getResponse();
    }

}
