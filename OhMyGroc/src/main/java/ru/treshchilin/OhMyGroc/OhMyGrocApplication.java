package ru.treshchilin.OhMyGroc;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ru.treshchilin.OhMyGroc.Model.Client;
import ru.treshchilin.OhMyGroc.Repo.ClientRepository;

@SpringBootApplication
public class OhMyGrocApplication {

	public static void main(String[] args) {
		SpringApplication.run(OhMyGrocApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ClientRepository repository) {
		return args -> {
			Client nick = new Client("equat@mail.com", "Equat", List.of("Milk", "Tomatoes", "Tea", "Bread", "Epica"));

			Client vova = new Client("vtresh@mail.com", "Vtresh", List.of("Milk", "Beer", "Bread", "Fish"));
			repository.saveAll(List.of(nick, vova));
		};

	}
}
