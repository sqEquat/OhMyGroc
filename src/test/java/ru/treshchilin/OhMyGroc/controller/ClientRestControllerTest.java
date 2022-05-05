package ru.treshchilin.OhMyGroc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ru.treshchilin.OhMyGroc.dto.ClientRegisterDto;
import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.service.ClientService;

@ExtendWith(MockitoExtension.class)
class ClientRestControllerTest {

	@Mock
	private ClientService clientService;
	private static Principal principal;
	
	@InjectMocks
	private ClientRestController underTest;
	
	@BeforeAll
	static void principalInit() {
		String username = "client";
		principal = mock(Principal.class);
		when(principal.getName()).thenReturn(username);
	}
	
	@Test
	void getClient_returnOkAndClient_ifPrincipalExists() {
		Client client = new Client();
		when(clientService.getClient(anyString())).thenReturn(client);
		
		ResponseEntity<Client> actualResponse = underTest.getClient(principal);
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(client);
	}
	
	@Test
	void updateClient_returnOkAndUpdatedClient_ifPrincipalAndRegisterDtoAreValid() {
		ClientRegisterDto registerDto = new ClientRegisterDto("client@mail.com", "client1", "password");
		Client updatedClient = new Client(registerDto.getEmail(), registerDto.getUsername(), registerDto.getPassword());
		when(clientService.updateClient(
				anyString(), 
				eq(registerDto.getEmail()), 
				eq(registerDto.getUsername()), 
				eq(registerDto.getPassword())))
		.thenReturn(updatedClient);
		
		ResponseEntity<Client> actualResponse = underTest.updateClient(principal, registerDto);
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(updatedClient);
	}

}
