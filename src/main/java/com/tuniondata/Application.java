package com.tuniondata;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yinheli
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.tuniondata" })
public class Application {

    public static void main(String[] args) {
        new SpringApplication(Application.class).run(args);
    }
}

