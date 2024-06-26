package com.roundtable.roundtable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@ConfigurationPropertiesScan
@SpringBootApplication
public class RoundTableApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoundTableApplication.class, args);
	}

}
