package com.jwss.studios.loja_virtual_BackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = "com.jwss.studios.loja_virtual_BackEnd.model")
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories(basePackages = {"com.jwss.studios.loja_virtual_BackEnd.repository"})
@EnableTransactionManagement
public class LojaVirtualBackEndApplication implements AsyncConfigurer {

    public static void main(String[] args) {


        SpringApplication.run(LojaVirtualBackEndApplication.class, args);
        //System.out.println(new BCryptPasswordEncoder().encode("123"));
    }
    @Override
    @Bean
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Assyncrono Thread");
        executor.initialize();
        return executor;

    }

}
