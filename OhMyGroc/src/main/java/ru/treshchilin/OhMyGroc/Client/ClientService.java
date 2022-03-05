package ru.treshchilin.OhMyGroc.Client;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ClientService {
	
	public List<Client> getUsers() {
		return List.of(
				new Client(1L, "equat@mail.com", "Equat", List.of("Milk", "Tomatoes", "Tea", "Bread", "Epica")));
	}
}
