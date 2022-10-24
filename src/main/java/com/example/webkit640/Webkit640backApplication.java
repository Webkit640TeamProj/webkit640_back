package com.example.webkit640;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Webkit640backApplication {

	public static void main(String[] args) {
		SpringApplication.run(Webkit640backApplication.class, args);
	}

}
