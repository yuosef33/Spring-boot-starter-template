package com.yuosef.springbootstartertemplate;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringBootStarterTemplate {

    public static void main(String[] args) {
        SpringApplication.run(com.yuosef.springbootstartertemplate.SpringBootStarterTemplate.class, args);
    }

}
