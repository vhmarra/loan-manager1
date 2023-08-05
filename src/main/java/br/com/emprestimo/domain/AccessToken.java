package br.com.emprestimo.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "access_token")
@Data
public class AccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "acess_token_value")
    private String token;

    @Column(name = "active")
    private Boolean isActive;

    @OneToOne
    UserEntity user;

}
