package com.example.springpersonwebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.example.springpersonwebflux.security.config" })
public class SpringPersonWebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringPersonWebfluxApplication.class, args);
	}

}
