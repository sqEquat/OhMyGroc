package ru.treshchilin.OhMyGroc.Client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

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
	void testDeleteClientIfExists() {
		Long clientId = 1L;
		when(clientRepository.existsById(clientId)).thenReturn(true);
		
		underTest.deleteClient(clientId);
		
		verify(clientRepository).deleteById(clientId);
	}

	@Test
	@Disabled
	void testUpdateClient() {
		fail("Not yet implemented");
	}

}
