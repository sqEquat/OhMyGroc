package ru.treshchilin.OhMyGroc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;

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
	public ResponseEntity<Client> getClient(
			@RequestHeader(name = "Authorization") String authorizationHeader) {
		String username = usernameFromAuthorizationHeader(authorizationHeader);
		
		return ResponseEntity.ok().body(clientService.getClient(username));
	}
	
	private String usernameFromAuthorizationHeader(String authorizationHeader) {
		return JWT.decode(authorizationHeader.substring("Bearer ".length())).getSubject();
	}
}
