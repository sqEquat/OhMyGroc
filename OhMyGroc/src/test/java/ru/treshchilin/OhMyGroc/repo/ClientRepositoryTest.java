package ru.treshchilin.OhMyGroc.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ru.treshchilin.OhMyGroc.model.Client;

@DataJpaTest
class ClientRepositoryTest {
	
	@Autowired
	private ClientRepository clientRepository;
	
	@AfterEach
	void tearDown() {
		clientRepository.deleteAll();
	}

	@Test
	void testFindByEmailIfClientExist() {
		Client client = new Client("equat@gmail.com", "Equat");
		clientRepository.save(client);
		
		List<Client> result = clientRepository.findByEmail(client.getEmail());
		
		assertThat(result).isNotEmpty();
	}
	
	@Test
	void testFindByEmailIfClientDoesNotExists() {
		String email = "equat@gmail.com";
		List<Client> result = clientRepository.findByEmail(email);
		assertThat(result).isEmpty();
	}

}
