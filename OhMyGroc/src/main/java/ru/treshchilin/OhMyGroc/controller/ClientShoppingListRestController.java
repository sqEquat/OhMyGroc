package ru.treshchilin.OhMyGroc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;

import ru.treshchilin.OhMyGroc.model.ShoppingList;
import ru.treshchilin.OhMyGroc.service.ClientService;

@RestController
@RequestMapping("/api/v2/client/lists")
public class ClientShoppingListRestController {
	
	private final ClientService clientService;
	
	@Autowired
	public ClientShoppingListRestController(ClientService clientService) {
		super();
		this.clientService = clientService;
	}
	
	
	/*
	 * Get all client's shopping lists
	 */
	@GetMapping
	public ResponseEntity<List<ShoppingList>> getShoppingLists(
			@RequestHeader(name = "Authorization") String authorizationHeader) {
		String username = usernameFromAuthorizationHeader(authorizationHeader);
		
		return ResponseEntity.ok().body(clientService.getShoppingLists(username));
	}
	
	/*
	 * Get shopping list with provided id
	 */	
	@GetMapping("/{listId}")
	public ResponseEntity<ShoppingList> getShoppingList(
			@RequestHeader(name = "Authorization") String authorizationHeader,
			@PathVariable(name = "listId", required = true) Long listId) {
		String username = usernameFromAuthorizationHeader(authorizationHeader);
		
		return ResponseEntity.ok().body(clientService.getShoppingListById(username, listId));
	}

	/*
	 * Add new shopping list to the client 
	 */
	@PostMapping
	public ResponseEntity<ShoppingList> addNewShoppingList(
			@RequestHeader(name = "Authorization") String authorizationHeader,
			@RequestBody(required = true) ShoppingList shoppingList) {
		String username = usernameFromAuthorizationHeader(authorizationHeader);

		return ResponseEntity.ok().body(clientService.addNewShoppingList(username, shoppingList));
	}
	
	/*
	 * Update the shopping list with provided id
	 */
	@PutMapping("/{listId}")
	public ResponseEntity<ShoppingList> updateShoppingList(
			@RequestHeader(name = "Authorization") String authorizationHeader,
			@PathVariable(name = "listId", required = true) Long listId,
			@RequestBody(required = true) ShoppingList shoppingList) {
		String username = usernameFromAuthorizationHeader(authorizationHeader);
		
		return ResponseEntity.ok().body(clientService.updateShoppingList(username, listId, shoppingList));
	}
	
	/*
	 * Delete shopping list with provided id
	 */
	@DeleteMapping("/{listId}")
	public ResponseEntity<?> deleteShoppingList(
			@RequestHeader(name = "Authorization") String authorizationHeader,
			@PathVariable(name = "listId", required = true) Long listId) {
		String username = usernameFromAuthorizationHeader(authorizationHeader);
		
		return ResponseEntity.ok().body(clientService.deleteShoppingList(username, listId));
	}

	private String usernameFromAuthorizationHeader(String authorizationHeader) {
		return JWT.decode(authorizationHeader.substring("Bearer ".length())).getSubject();
	}
}
