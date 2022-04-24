package ru.treshchilin.OhMyGroc.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<List<ShoppingList>> getShoppingLists(Principal principal) {
		return ResponseEntity.ok().body(clientService.getShoppingLists(principal.getName()));
	}
	
	/*
	 * Get shopping list with provided id
	 */	
	@GetMapping("/{listId}")
	public ResponseEntity<ShoppingList> getShoppingList(
			Principal principal,
			@PathVariable(name = "listId", required = true) Long listId) {
		return ResponseEntity.ok().body(clientService.getShoppingListById(principal.getName(), listId));
	}

	/*
	 * Add new shopping list to the client 
	 */
	@PostMapping
	public ResponseEntity<ShoppingList> addNewShoppingList(
			Principal principal,
			@RequestBody(required = true) ShoppingList shoppingList) {
		return ResponseEntity.ok().body(clientService.addNewShoppingList(principal.getName(), shoppingList));
	}
	
	/*
	 * Update the shopping list with provided id
	 */
	@PutMapping("/{listId}")
	public ResponseEntity<ShoppingList> updateShoppingList(
			Principal principal,
			@PathVariable(name = "listId", required = true) Long listId,
			@RequestBody(required = true) ShoppingList shoppingList) {
		return ResponseEntity.ok().body(clientService.updateShoppingList(principal.getName(), listId, shoppingList));
	}
	
	/*
	 * Delete shopping list with provided id
	 */
	@DeleteMapping("/{listId}")
	public ResponseEntity<?> deleteShoppingList(
			Principal principal,
			@PathVariable(name = "listId", required = true) Long listId) {
		return ResponseEntity.ok().body(clientService.deleteShoppingList(principal.getName(), listId));
	}
}
