package ru.treshchilin.OhMyGroc.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ru.treshchilin.OhMyGroc.dto.ClientRegisterDto;
import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.service.ClientService;

@RestController
@RequestMapping("/api/v2/register")
public class ClientRegisterRestController {

	private final ClientService clientService;

	@Autowired
	public ClientRegisterRestController(ClientService clientService) {
		this.clientService = clientService;
	}
	
	@PostMapping
	public ResponseEntity<Client> registerNewClient(@Valid @RequestBody ClientRegisterDto registerDto) {		
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v2/register").toUriString());
		return ResponseEntity.created(uri).body(clientService.addNewClient(registerDto));
	}
	
}
