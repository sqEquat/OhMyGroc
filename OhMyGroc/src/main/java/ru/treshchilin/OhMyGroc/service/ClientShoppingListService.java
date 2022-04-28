package ru.treshchilin.OhMyGroc.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.model.ShoppingList;
import ru.treshchilin.OhMyGroc.repo.ClientRepository;

@Service
public class ClientShoppingListService {

	private final ClientRepository clientRepository;

	@Autowired
	public ClientShoppingListService(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}
	
	public List<ShoppingList> getShoppingLists(String username) {
		return clientRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"))
				.getShopLists();
	}
	
	public ShoppingList getShoppingListById(String username, Long listId) {
		return clientRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"))
				.getShopLists().stream()
				.filter(list -> list.getId()
				.equals(listId))
				.findAny()
				.orElseThrow(() -> new IllegalStateException("List with id=" + listId + " not found!"));
	}
	
	@Transactional
	public ShoppingList addNewShoppingList(String username, ShoppingList shoppingList) {
		Client client = clientRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
		
		shoppingList.setDateCreated(LocalDateTime.now());
		shoppingList.setClient(client);
		
		client.addShoppingList(shoppingList);
		
		return shoppingList;
	}
	@Transactional
	public ShoppingList updateShoppingList(String username, Long listId, ShoppingList shoppingList) {
		ShoppingList listToUpdate = clientRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"))
				.getShopLists().stream()
				.filter(id -> id.getId().equals(listId))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("List with id=" + listId + " not found!"));
		
		listToUpdate.setItems(shoppingList.getItems());
		
		return listToUpdate;
	}
	
	@Transactional
	public String deleteShoppingList(String username, Long listId) {
		Client client = clientRepository.findByUsername(username).orElseThrow();
		
		if (client.getShopLists().removeIf(list -> list.getId().equals(listId)))
			return "Shopping list with id: " + listId + " was deleted";
		else
			return "There is no shopping list with id: " + listId;
	}
}
