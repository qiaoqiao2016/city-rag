package com.cityrag.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.cityrag")
public class CityRagGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CityRagGatewayApplication.class, args);
    }
}
