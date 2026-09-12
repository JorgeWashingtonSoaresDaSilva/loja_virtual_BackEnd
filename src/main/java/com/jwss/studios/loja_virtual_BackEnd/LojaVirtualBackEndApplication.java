package com.jwss.studios.loja_virtual_BackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(basePackages = "com.jwss.studios.loja_virtual_BackEnd.model")
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories(basePackages = {"com.jwss.studios.loja_virtual_BackEnd.repository"})
@EnableTransactionManagement
public class LojaVirtualBackEndApplication {

    public static void main(String[] args) {


        SpringApplication.run(LojaVirtualBackEndApplication.class, args);
        //System.out.println(new BCryptPasswordEncoder().encode("123"));
    }

}
