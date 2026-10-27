package com.djole.prodavnica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ProdavnicaIgaraApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ProdavnicaIgaraApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ProdavnicaIgaraApplication.class);
    }
}
