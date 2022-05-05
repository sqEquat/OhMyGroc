package ru.treshchilin.OhMyGroc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ru.treshchilin.OhMyGroc.dto.ClientRegisterDto;
import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.service.ClientService;

@ExtendWith(MockitoExtension.class)
class ClientRegisterRestControllerTest {

	@Mock
	ClientService clientServicel;
	
	@InjectMocks
	ClientRegisterRestController underTest;
	
	@Test
	void registerNewClient_returnCreatedAndClient_ifRegisterDtoIsValid() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		ClientRegisterDto registerDto = new ClientRegisterDto("client@mail.com", "username", "password");
		Client registeredClient = new Client(registerDto.getEmail(), registerDto.getUsername(), registerDto.getPassword());
		when(clientServicel.addNewClient(registerDto)).thenReturn(registeredClient);
		
		ResponseEntity<Client> actualResponse = underTest.registerNewClient(registerDto);
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(actualResponse.getHeaders().getLocation().getPath()).isEqualTo("/api/v2/client");
		assertThat(actualResponse.getBody()).isEqualTo(registeredClient);
	}

}
