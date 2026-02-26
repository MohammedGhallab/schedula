package com.schedula.schedula;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@EnableJpaRepositories(basePackages = "com.schedula.schedula.*.repositories")
@SpringBootApplication
public class SchedulaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulaApplication.class, args);
	}

}
