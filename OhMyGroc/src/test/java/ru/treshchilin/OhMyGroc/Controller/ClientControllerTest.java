package ru.treshchilin.OhMyGroc.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.treshchilin.OhMyGroc.Model.Client;
import ru.treshchilin.OhMyGroc.Service.ClientService;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

	@Mock
	private ClientService clientService;
	
	@InjectMocks
	private ClientController underTest;
	
	@Test
	void testGetClients() {
		List<Client> clients = List.of(
				new Client(1L, "test1@test.com", "Test1", Collections.emptyList()),
				new Client(2L, "test2@test.com", "Test2", Collections.emptyList()),
				new Client(3L, "test3@test.com", "Test3", Collections.emptyList()));
		when(clientService.getClients()).thenReturn(clients);
		
		List<Client> gotClients = underTest.getClients().getBody();
		
		assertThat(gotClients).isEqualTo(clients);
		verify(clientService).getClients();
	}
	
	@Test
	void getClientIfExists() {
		Client client = new Client(1L, "test@test.com", "Test", Collections.emptyList());
		when(clientService.getClient(client.getId())).thenReturn(client);
		
		Client gotClient = underTest.getClient(client.getId()).getBody();
		
		assertThat(gotClient).isEqualTo(client);
		verify(clientService, atMostOnce()).getClient(client.getId());
	}

	@Test
	void testRegisterNewClient() {
		Client client = new Client("test1@test.com", "Test1");
		
		underTest.registerNewClient(client);
		
		verify(clientService).addNewClient(client);
	}

	@Test
	void testUpdateClient() {
		Client client = new Client(1L, "test@test.com", "Test", Collections.emptyList());
		
		underTest.updateClient(client.getId(), client.getUsername(), client.getEmail());
		
		verify(clientService).updateClient(client.getId(), client.getUsername(), client.getEmail());
	}

	@Test
	void testDeleteClient() {
		Long clientId = 1L;
		
		underTest.deleteClient(clientId);
		
		verify(clientService).deleteClient(clientId);
	}

}
