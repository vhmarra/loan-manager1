package br.com.emprestimo.domain;

import br.com.emprestimo.dtos.UserSignUpRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_tb")
@Data
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_cpf",unique = true)
    private String cpf;

    @Column(name = "user_email",unique = true)
    private String email;

    @Column(name = "user_name")
    private String name;

    @Column(name = "date_sign")
    private LocalDateTime dateSigned;

    @Column(name = "is_user_active")
    private Boolean isUserActive;

    @Column(name = "serase_score")
    @Min(value = 0)
    @Max(value = 1000)
    private int serasaScore;

    public UserEntity(UserSignUpRequest request) {
        this.cpf = request.getCpf();
        this.email = request.getEmail();
        this.name = request.getName();
        this.dateSigned = LocalDateTime.now();
        this.isUserActive = Boolean.FALSE; //All users must me activated before request loans
        this.setSerasaScore(request.getSerasaScore());
    }

}
