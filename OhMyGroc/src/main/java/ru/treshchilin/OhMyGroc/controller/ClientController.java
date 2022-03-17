package ru.treshchilin.OhMyGroc.controller;

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

import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.model.ShoppingList;
import ru.treshchilin.OhMyGroc.service.ClientService;

@RestController
@RequestMapping("api/v1/client")
@Validated
public class ClientController {

	private final ClientService clientService;
	
	@Autowired
	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}
	
	// Methods for manipulating clients
	
	@GetMapping
	public ResponseEntity<List<Client>> getClients() {
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(clientService.getClients());
	}
	
	@GetMapping("/{clientId}")
	public ResponseEntity<Client> getClient(
			@PathVariable("clientId") @Min(1) Long id) {
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
	public ResponseEntity<Client> updateClient(
			@PathVariable("clientId") @Min(1) Long clientId,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String email) {		
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(clientService.updateClient(clientId, username, email));
	}
	
	@DeleteMapping("/{clientId}")
	public ResponseEntity<String> deleteClient(
			@PathVariable("clientId") @Min(1) Long clientId) {
		clientService.deleteClient(clientId);
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body("Client with id: " + clientId + " was deleted");
	}
	
	
	// Methods for manipulating clients shopping lists
	
	@GetMapping("/{clientId}/{listId}")
	public ResponseEntity<ShoppingList> getSpecificList(
			@PathVariable("clientId") @Min(1) Long clientId,
			@PathVariable("listId") @Min(1) Long listId) {
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(clientService.getShoppingListById(clientId, listId));
	}
	
	@PostMapping("/{clientId}")
	public ResponseEntity<Client> addNewShoppingList(
			@PathVariable("clientId") @Min(1) Long clientId,
			@Valid @RequestBody ShoppingList shoppingList) {
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(clientService.addNewClientShoppingList(clientId, shoppingList));
	}
	
	@PutMapping("/{clientId}/{listId}")
	public ResponseEntity<ShoppingList> updateShoppingList(
			@PathVariable("clientId") @Min(1) Long clientId,
			@PathVariable("listId") @Min(1) Long listId,
			@RequestBody ShoppingList shoppingList) {
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(clientService.updateShoppingList(clientId, listId, shoppingList));
	}
	
	@DeleteMapping("/{clientId}/{listId}")
	public ResponseEntity<String> deleteShoppingList(
			@PathVariable("clientId") @Min(1) Long clientId,
			@PathVariable("listId") @Min(1) Long listId) {
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(clientService.deleteShoppingList(clientId, listId));
	}

}
