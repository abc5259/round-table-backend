package com.roundtable.roundtable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RoundTableApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoundTableApplication.class, args);
	}

}
