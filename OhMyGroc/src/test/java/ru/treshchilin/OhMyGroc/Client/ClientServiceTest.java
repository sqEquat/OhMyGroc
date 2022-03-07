package ru.treshchilin.OhMyGroc.Client;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

	@Mock
	private ClientRepository clientRepository;
	private ClientService underTest;
	
	@BeforeEach
	void setUp() throws Exception {
		underTest = new ClientService(clientRepository);
	}

	@Test
	void testGetClients() {
		underTest.getClients();
		
		verify(clientRepository).findAll();
	}

	@Test
	void testAddNewClientIfEmailIsFree() {
		Client client = new Client("test@test.com", "Test");
		
		underTest.addNewClient(client);
		
		verify(clientRepository).save(client);
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
	@Disabled
	void testUpdateClient() {
		fail("Not yet implemented");
	}

}
