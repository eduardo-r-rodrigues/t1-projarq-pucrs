package com.eduardo.projarq.t1.salesregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SalesRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(SalesRegistryApplication.class, args);
    }
} 