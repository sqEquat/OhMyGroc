package ru.treshchilin.OhMyGroc.Service;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.treshchilin.OhMyGroc.Model.Client;
import ru.treshchilin.OhMyGroc.Repo.ClientRepository;

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

}
