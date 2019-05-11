package com.pergamon.Pergamnon;

import com.pergamon.Pergamnon.v1.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class PergamnonApplication {

	public static void main(String[] args) {
		SpringApplication.run(PergamnonApplication.class, args);
	}

}
