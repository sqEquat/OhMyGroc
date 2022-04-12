package ru.treshchilin.OhMyGroc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.model.Role;
import ru.treshchilin.OhMyGroc.service.ClientService;

@SpringBootApplication
public class OhMyGrocApplication {

	public static void main(String[] args) {
		SpringApplication.run(OhMyGrocApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(ClientService clientService) {
		return args -> {
			clientService.saveRole(new Role(null, "ROLE_ADMIN"));
			
			clientService.addNewClient(new Client(null, "test1@test.com", "Test1", "Password1", null, null));
			
			clientService.addRoleToClient(1L, 1L);
		};
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
