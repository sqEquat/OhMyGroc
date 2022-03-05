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
		System.out.println(client);
	}
}
