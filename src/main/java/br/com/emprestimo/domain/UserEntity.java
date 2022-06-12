package br.com.emprestimo.domain;

import lombok.Data;

import javax.persistence.*;

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



}
