package ru.treshchilin.OhMyGroc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.model.ShoppingList;
import ru.treshchilin.OhMyGroc.repo.ClientRepository;

@Service
public class ClientService {
	
	private final ClientRepository clientRepository;
	
	@Autowired
	public ClientService(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	public List<Client> getClients() {
		return clientRepository.findAll();
	}

	public Client getClient(Long id) {
		return clientRepository.findById(id).orElseThrow();
	}
	
	public Client addNewClient(Client client) {
		List<Client> clientByEmail =  clientRepository.findByEmail(client.getEmail());
		if (!clientByEmail.isEmpty()) {
			throw new IllegalStateException("Email is already exists");
		}
		
		return clientRepository.save(client);
	}

	public void deleteClient(Long clientId) {
		if (!clientRepository.existsById(clientId)) {
			throw new IllegalStateException("Client with id " + clientId + " does not exists");
		}
		
		clientRepository.deleteById(clientId);
	}

	@Transactional
	public Client updateClient(Long clientId, String username, String email) {
		Client client = clientRepository.findById(clientId)
				.orElseThrow(() -> new IllegalStateException(
				"Client with id " + clientId + " does not exists"));
		
		if (username != null &&
				username.length() > 0 &&
				!Objects.equals(client.getUsername(), username)) {
			client.setUsername(username);
		}
		
		if (email != null &&
				email.length() > 0 &&
				!Objects.equals(client.getEmail(), email)) {
			if(!clientRepository.findByEmail(email).isEmpty()) {
				throw new IllegalStateException("Email is already exists");
			}
			
			client.setEmail(email);
		}
		
		return client;
	}

	@Transactional
	public Client addNewClientShoppingList(Long clientId, ShoppingList shoppingList) {
		Client client = clientRepository.findById(clientId).orElseThrow();
		
		shoppingList.setDateCreated(LocalDateTime.now());
		shoppingList.setClient(client);
		
		client.addShoppingList(shoppingList);
		
		return client;
	}

	@Transactional
	public ShoppingList updateShoppingList(Long clientId, Long listId, ShoppingList shoppingList) {
		ShoppingList listToUpdate = clientRepository.getById(clientId).getShopLists().stream()
				.filter(id -> id.getId()
				.equals(listId))
				.findFirst()
				.orElseThrow();
		
		if (listToUpdate != null)
			listToUpdate.setItems(shoppingList.getItems());
		
		return listToUpdate;
	}

	@Transactional
	public String deleteShoppingList(Long clientId, Long listId) {
		Client client = clientRepository.findById(clientId).orElseThrow();
		
		if (client.getShopLists().removeIf(list -> list.getId().equals(listId)))
			return "Shopping list with id: " + listId + " was deleted";
		else
			return "There is no shopping list with id: " + listId;
	}

	public ShoppingList getShoppingListById(Long clientId, Long listId) {
		return clientRepository.findById(clientId).orElseThrow().getShopLists().stream()
				.filter(list -> list.getId()
				.equals(listId))
				.findAny()
				.orElseThrow();
	}

}
