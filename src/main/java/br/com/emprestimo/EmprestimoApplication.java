package br.com.emprestimo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class EmprestimoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmprestimoApplication.class, args);
    }

}
