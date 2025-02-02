package com.ll.sapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Sapp7Application {

    public static void main(String[] args) {
        SpringApplication.run(Sapp7Application.class, args);
    }

}
