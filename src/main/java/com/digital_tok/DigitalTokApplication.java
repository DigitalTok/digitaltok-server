package com.digital_tok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DigitalTokApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalTokApplication.class, args);
	}

}
