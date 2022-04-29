package ru.treshchilin.OhMyGroc.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import ru.treshchilin.OhMyGroc.dto.ClientRegisterDto;
import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.model.Role;
import ru.treshchilin.OhMyGroc.repo.ClientRepository;
import ru.treshchilin.OhMyGroc.repo.RoleRepository;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

	@Mock
	private ClientRepository clientRepository;
	
	@Mock
	private RoleRepository roleRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
	private ClientService underTest;

	@ParameterizedTest
	@MethodSource("existingClientsDataSource")
	void getClientIfExists(String username, Client client) {
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		Client gotClient = underTest.getClient(username);
		
		verify(clientRepository, atMostOnce()).findByUsername(username);
		assertThat(gotClient).isEqualTo(client);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"username", "notusername", "not a username"})
	void getClientIfNotExists(String username) {
		when(clientRepository.findByUsername(any())).thenReturn(Optional.empty());
		
		Exception ex = assertThrows(UsernameNotFoundException.class, () -> underTest.getClient(username));
		
		assertThat(ex.getMessage()).contains("Username",  username, "not found");
	}
	
	@ParameterizedTest
	@EmptySource
	@ValueSource(strings = {"\t", "\n", "  "})
	void getClientIfUsernameIsBlank(String username) {
		when(clientRepository.findByUsername(any())).thenReturn(Optional.empty());
		
		Exception ex = assertThrows(UsernameNotFoundException.class, () -> underTest.getClient(username));
		
		assertThat(ex.getMessage()).contains("Username",  username, "not found");
	}
	
	private static Stream<Arguments> existingClientsDataSource() {
		return Stream.of(
				Arguments.of("client", new Client(1L, "client@test.com", "client", "password", List.of(), List.of(new Role(1L, "ROLE_CLIENT")))),
				Arguments.of("admin", new Client(1L, "admin@test.com", "admin", "password", List.of(), List.of(new Role(2L, "ROLE_ADMIN")))));
	}
	
	@ParameterizedTest
	@MethodSource("newClientsDataSource")
	void addNewClientWithCorrectData(ClientRegisterDto clientRegisterDto) {
		when(clientRepository.findByUsername(any())).thenReturn(Optional.empty());
		when(clientRepository.findByEmail(any())).thenReturn(Optional.empty());
		when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role(2L, "ROLE_CLIENT")));
		
		underTest.addNewClient(clientRegisterDto);
		
		ArgumentCaptor<Client> registerClientCaptor = ArgumentCaptor.forClass(Client.class);
		verify(clientRepository, atMostOnce()).save(registerClientCaptor.capture());
		Client clientToSave = registerClientCaptor.getValue();
		
		assertThat(clientToSave.getEmail()).isEqualTo(clientRegisterDto.getEmail());
		assertThat(clientToSave.getUsername()).isEqualTo(clientRegisterDto.getUsername());
//		TODO here need to check if the password encoded correctly
		assertThat(clientToSave.getRoles().size()).isEqualTo(1);
		clientToSave.getRoles().forEach(r -> assertThat(r.getName()).isEqualTo("ROLE_CLIENT"));
	}
	
	@Test
	void addnewClientIfUsernameExists() {
		when(clientRepository.findByUsername(any())).thenReturn(Optional.of(new Client()));
		
		Exception ex = assertThrows(
				IllegalStateException.class, 
				() -> underTest.addNewClient(new ClientRegisterDto("email@text.com", "existedUsername", "password")));
		
		assertThat(ex.getMessage()).contains("Username is already taken");
	}
	
	@Test
	void addNewClientIfEmailExists() {
		when(clientRepository.findByUsername(any())).thenReturn(Optional.empty());
		when(clientRepository.findByEmail(any())).thenReturn(Optional.of(new Client()));
		
		Exception ex = assertThrows(
				IllegalStateException.class, 
				() -> underTest.addNewClient(new ClientRegisterDto("email@text.com", "existedUsername", "password")));
		
		assertThat(ex.getMessage()).contains("Email is already taken");
	}
	
	private static Stream<ClientRegisterDto> newClientsDataSource() {
		return Stream.of(
						new ClientRegisterDto("client1@test.com", "client1", "password1"), 
						new ClientRegisterDto("client2@test.com", "client2", "password2"), 
						new ClientRegisterDto("client3@test.com", "client3", "password3") 
				);
	}
	
	@Test
	void updateClientEmail() {
		Client client = new Client(1L, "client1@mail.com", "client1", "password1", List.of(), List.of(new Role(2L, "ROLE_CLIENT")));
		String newEmail = "updated@mail.com";
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		Client updatedClient = underTest.updateClient(client.getUsername(), newEmail, null, null);
		
		assertThat(updatedClient.getEmail()).isEqualTo(newEmail);
		assertThat(updatedClient).isEqualTo(client);
	}
	
}
