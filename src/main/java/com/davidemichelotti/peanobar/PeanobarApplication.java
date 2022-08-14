package com.davidemichelotti.peanobar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.davidemichelotti.peanobar")
public class PeanobarApplication extends SpringBootServletInitializer {
    //TODO add error handling
    public static void main(String[] args) {
        SpringApplication.run(PeanobarApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PeanobarApplication.class);
    }

}
