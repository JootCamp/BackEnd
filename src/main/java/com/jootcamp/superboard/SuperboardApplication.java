package com.jootcamp.superboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SuperboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuperboardApplication.class, args);
    }

}
