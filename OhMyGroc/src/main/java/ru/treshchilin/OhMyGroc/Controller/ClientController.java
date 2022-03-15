package ru.treshchilin.OhMyGroc.Controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.treshchilin.OhMyGroc.Model.Client;
import ru.treshchilin.OhMyGroc.Service.ClientService;

@RestController
@RequestMapping("api/v1/client")
@Validated
public class ClientController {

	private final ClientService clientService;
	
	@Autowired
	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}
	
	@GetMapping
	public ResponseEntity<List<Client>> getClients() {
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(clientService.getClients());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Client> getClient(@PathVariable("id") @Min(1) Long id) {
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(clientService.getClient(id));
	}
	
	@PostMapping
	public ResponseEntity<Client> registerNewClient(
			@Valid @RequestBody Client client) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.contentType(MediaType.APPLICATION_JSON)
				.body(clientService.addNewClient(client));
	}
	
	@PutMapping("/{clientId}")
	public void updateClient(
			@PathVariable("clientId") @Min(1) Long clientId,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String email) {
		clientService.updateClient(clientId, username, email);
	}
	
	@DeleteMapping("/{clientId}")
	public void deleteClient(
			@PathVariable("clientId") @Min(1) Long clientId) {
		clientService.deleteClient(clientId);
	}

}
