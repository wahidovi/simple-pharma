package com.simple.pharma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan({ "com.simple.pharma.source.controller", "com.simple.pharma.source.util",
		"com.simple.pharma.source.filter", "com.simple.pharma.source.service", "com.simple.pharma.config" })
@EntityScan("com.simple.pharma.source.model")
@EnableJpaRepositories("com.simple.pharma.source.repository")
public class ShoppingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingApplication.class, args);
	}
}
