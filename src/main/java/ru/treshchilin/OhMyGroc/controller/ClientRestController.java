package ru.treshchilin.OhMyGroc.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.treshchilin.OhMyGroc.dto.ClientRegisterDto;
import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.service.ClientService;

@RestController
@RequestMapping("/api/v2/client")
public class ClientRestController {

	private final ClientService clientService;

	@Autowired
	public ClientRestController(ClientService clientService) {
		this.clientService = clientService;
	}
	
	/*
	 * Return authenticated client information, token have to be already verified
	 */
	@GetMapping
	public ResponseEntity<Client> getClient(Principal principal) {
		return ResponseEntity.ok().body(clientService.getClient(principal.getName()));
	}
	
	
	/*
	 * Update client email, username and password
	 */
	@PutMapping
	public ResponseEntity<Client> updateClient(
			Principal principal,
			@Valid @RequestBody ClientRegisterDto registerDto) {
		return ResponseEntity.ok().body(clientService.updateClient(principal.getName(), 
				registerDto.getEmail(), 
				registerDto.getUsername(), 
				registerDto.getPassword()));
	}
}
