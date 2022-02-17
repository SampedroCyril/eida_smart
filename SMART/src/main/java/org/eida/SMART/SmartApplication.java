package org.eida.SMART;

import org.eida.SMART.m.UserDAO;
import org.eida.SMART.storage.StorageProperties;
import org.eida.SMART.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SmartApplication {

	private final UserDAO userDAO;

	public SmartApplication(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public static void main(String[] args) {
		SpringApplication.run(SmartApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}

}