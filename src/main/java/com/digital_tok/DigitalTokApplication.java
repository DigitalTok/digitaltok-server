package com.digital_tok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync // 비동기 처리 활성화
public class DigitalTokApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalTokApplication.class, args);
	}

}
