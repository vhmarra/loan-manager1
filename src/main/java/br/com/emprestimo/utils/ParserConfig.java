package br.com.emprestimo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserConfig {


    @Bean
    public Gson getParser() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .setLenient() // THIS can cause issues
                .create();
    }

}
