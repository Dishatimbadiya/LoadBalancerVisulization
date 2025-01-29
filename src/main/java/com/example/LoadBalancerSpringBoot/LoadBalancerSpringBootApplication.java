package com.example.LoadBalancerSpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoadBalancerSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoadBalancerSpringBootApplication.class, args);
        System.out.println("Application Started...");
    }
}