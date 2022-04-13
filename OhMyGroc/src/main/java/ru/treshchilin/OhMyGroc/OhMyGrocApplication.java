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
			clientService.saveRole(new Role(null, "ROLE_CLIENT"));
			
			clientService.addNewClient(new Client(null, "admin_1@test.com", "Admin_1", "Password_1", null, null));
			clientService.addNewClient(new Client(null, "client_1@test.com", "Client_1", "Password_2", null, null));
			
			clientService.addRoleToClient(1L, 1L);
			clientService.addRoleToClient(2L, 2L);
		};
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
