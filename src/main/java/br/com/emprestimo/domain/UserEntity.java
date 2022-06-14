package br.com.emprestimo.domain;

import br.com.emprestimo.dtos.UserSignUpRequest;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_tb")
@Data
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

    public UserEntity(UserSignUpRequest request) {
        this.cpf = request.getCpf();
        this.email = request.getEmail();
        this.name = request.getName();
        this.dateSigned = LocalDateTime.now();
        this.isUserActive = Boolean.TRUE;
    }


}
