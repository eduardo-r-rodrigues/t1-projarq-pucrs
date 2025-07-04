package com.eduardo.projarq.t1.taxservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TaxServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaxServiceApplication.class, args);
    }
} 