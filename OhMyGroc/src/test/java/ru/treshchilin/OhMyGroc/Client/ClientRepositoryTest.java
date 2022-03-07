package ru.treshchilin.OhMyGroc.Client;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ClientRepositoryTest {
	
	@Autowired
	private ClientRepository clientRepository;

	@Test
	void testFindByEmail() {
		Client client = new Client("equat@gmail.com", "Equat");
		clientRepository.save(client);
		
		List<Client> queryResult = clientRepository.findByEmail(client.getEmail());
		
		assertThat(queryResult).isNotEmpty();
	}

}
