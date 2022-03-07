package ru.treshchilin.OhMyGroc.Client;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

	@Bean
	CommandLineRunner commandLineRunner(ClientRepository repository) {
		return args -> {
					Client nick = new Client(
							"equat@mail.com", 
							"Equat", 
							List.of("Milk", "Tomatoes", "Tea", "Bread", "Epica"));
					
					Client vova = new Client(
							"vtresh@mail.com", 
							"Vtresh", 
							List.of("Milk", "Beer", "Bread", "Fish"));
					repository.saveAll(List.of(nick, vova));
		};
	}
}
