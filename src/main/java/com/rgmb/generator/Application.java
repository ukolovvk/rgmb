package com.rgmb.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс - стартовая точка приложения (аннотация SpringBootApplication)
 * Метод main запускает Spring Boot приложение
 */

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
