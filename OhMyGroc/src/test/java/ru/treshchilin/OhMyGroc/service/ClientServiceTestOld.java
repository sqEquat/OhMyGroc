package ru.treshchilin.OhMyGroc.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.model.ShoppingList;
import ru.treshchilin.OhMyGroc.repo.ClientRepository;

@ExtendWith(MockitoExtension.class)
class ClientServiceTestOld {

	@Mock
	private ClientRepository clientRepository;
	
	@InjectMocks
	private ClientService underTest;
	
//	@Befo
//	void beforeEach() {
//		
//	}

	@Test
	void testGetClientIfExist() {
		Client client = new Cli
	}
	
	@Test
	void testGetClients() {
		List<Client> clients = List.of(
				new Client(1L, "test1@test.com", "Test1", Collections.emptyList()),
				new Client(2L, "test2@test.com", "Test2", Collections.emptyList()),
				new Client(3L, "test3@test.com", "Test3", Collections.emptyList()));
		when(clientRepository.findAll()).thenReturn(clients);
		
		List<Client> gotClients = underTest.getClients();
		
		assertThat(gotClients).isEqualTo(clients);
		verify(clientRepository).findAll();
	}
	
	@Test
	void getClient() {
		Client client = new Client(1L, "test@test.com", "Test", Collections.emptyList());
		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		
		Client gotClient = underTest.getClient(client.getId());
		
		assertThat(gotClient).isEqualTo(gotClient);
		verify(clientRepository, atMostOnce()).getById(client.getId());
	}

	@Test
	void testAddNewClientIfEmailIsFree() {
		Client client = new Client("test@test.com", "Test");
		
		underTest.addNewClient(client);
		
		ArgumentCaptor<Client> clientArgumentCaptor = ArgumentCaptor.forClass(Client.class);
		verify(clientRepository).save(clientArgumentCaptor.capture());
		Client capturedClient = clientArgumentCaptor.getValue();
		
		assertThat(capturedClient).isEqualTo(client);
	}
	
	@Test
	void testAddNewClientIfEmailIsOccupied() {
		Client client = new Client("test@test.com", "Test");
		when(clientRepository.findByEmail(client.getEmail())).thenReturn(List.of(client));
		
		assertThrows(IllegalStateException.class, () -> {
			underTest.addNewClient(client);
		});
		
		verify(clientRepository, never()).save(client);
	}

	@Test
	void testDeleteClientIfExists() {
		Long clientId = 1L;
		when(clientRepository.existsById(clientId)).thenReturn(true);
		
		underTest.deleteClient(clientId);
		
		verify(clientRepository).deleteById(clientId);
	}
	
	@Test
	void testDeleteClientIfDoesNotExists() {
		Long clientId = 1L;
		when(clientRepository.existsById(clientId)).thenReturn(false);
		
		
		assertThrows(IllegalStateException.class, () -> {
			underTest.deleteClient(clientId);
		});
		
		verify(clientRepository, never()).deleteById(clientId);
	}

	@Test
	void testUpdateClientIfIdNotFound() {
		Client client = new Client(1L, "test@test.com", "Test1", Collections.emptyList());
		when(clientRepository.findById(client.getId())).thenReturn(Optional.empty());
		
		assertThrows(IllegalStateException.class, () -> {
			underTest.updateClient(client.getId(), client.getEmail(), client.getUsername());
		});
	}
	
	@Test
	void testUpdateClientIfIdIsFoundAndEmailIsOccupied() {
		Client client = new Client(1L, "test@test.com", "Test1", Collections.emptyList());
		String newEmail = "new@test.com";
		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		when(clientRepository.findByEmail(newEmail)).thenReturn(List.of(new Client()));
		
		assertThrows(IllegalStateException.class, () -> {
			underTest.updateClient(client.getId(), null, newEmail);
		});
	}
	
	@Test
	void testUpdateClientName() {
		Client client = new Client(1L, "test@test.com", "Test1", Collections.emptyList());
		String newName = "Test2";
		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		
		underTest.updateClient(client.getId(), newName, null);
		
		assertThat(client.getUsername()).isEqualTo(newName);
	}
	
	@Test
	void testUpdateClientEmail() {
		Client client = new Client(1L, "test@test.com", "Test1", Collections.emptyList());
		String newEmail = "test2@test.com";
		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		
		underTest.updateClient(client.getId(), null, newEmail);
		
		assertThat(client.getEmail()).isEqualTo(newEmail);
	}
	
	@Test
	void addNewClientShoppingList() {
		Client client = new Client(1L, "test@test.com", "Test1", new ArrayList<ShoppingList>());
		ShoppingList shoppingList = new ShoppingList(null, null, List.of("Meat", "Milk", "Water", "Bread"));
		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		
		Client returrnedClient = underTest.addNewClientShoppingList(client.getId(), shoppingList);
		
		assertThat(shoppingList.getDateCreated()).isNotNull();
		assertThat(shoppingList.getClient()).isEqualTo(client);
		assertThat(returrnedClient).isEqualTo(client);
		assertThat(client.getShopLists()).contains(shoppingList);
	}

}
