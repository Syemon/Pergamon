package com.pergamon.Pergamon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PergamonApplication {

	public static void main(String[] args) {
		SpringApplication.run(PergamonApplication.class, args);
	}

}
