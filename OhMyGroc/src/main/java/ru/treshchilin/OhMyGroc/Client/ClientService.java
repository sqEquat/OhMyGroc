package ru.treshchilin.OhMyGroc.Client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public void addNewClient(Client client) {
		List<Client> clientByEmail =  clientRepository.findByEmail(client.getEmail());
		if (!clientByEmail.isEmpty()) {
			throw new IllegalStateException("Email is already exists");
		}
		
		clientRepository.save(client);
	}

	public void deleteClient(Long clientId) {
		if (!clientRepository.existsById(clientId)) {
			throw new IllegalStateException("Client with id " + clientId + " does not exists");
		}
		
		clientRepository.deleteById(clientId);
	}
}
