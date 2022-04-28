package ru.treshchilin.OhMyGroc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ru.treshchilin.OhMyGroc.dto.ClientRegisterDto;
import ru.treshchilin.OhMyGroc.model.Role;
import ru.treshchilin.OhMyGroc.service.AdminClientService;
import ru.treshchilin.OhMyGroc.service.ClientService;

@SpringBootApplication
public class OhMyGrocApplication {

	public static void main(String[] args) {
		SpringApplication.run(OhMyGrocApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(ClientService clientService, AdminClientService adminClientService) {
		return args -> {
			adminClientService.saveRole(new Role(null, "ROLE_ADMIN"));
			adminClientService.saveRole(new Role(null, "ROLE_CLIENT"));
			
			clientService.addNewClient(new ClientRegisterDto("admin_1@test.com", "admin", "admin"));
			clientService.addNewClient(new ClientRegisterDto("client_1@test.com", "client1", "password"));
			
			adminClientService.addRoleToClient(1L, 1L);
			adminClientService.addRoleToClient(2L, 2L);
		};
	}
	
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
