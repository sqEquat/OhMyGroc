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
import org.junit.jupiter.params.provider.NullAndEmptySource;
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

//	getClient
	
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
	
//	addNewClient
	
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
	
//	updateClient
	
	@Test
	void updateClientEmailIfValid() {
		Client client = new Client("client1@mail.com", "client1", "password1");
		String newEmail = "newEmail@test.com";
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		Client updatedClient = underTest.updateClient(client.getUsername(), newEmail, null, null);
		
		assertThat(updatedClient.getEmail()).isEqualTo(newEmail);
		assertThat(updatedClient).isEqualTo(client);
	}
	
	@Test
	void updateClientEmailIfExists() {
		Client client = new Client("client1@mail.com", "client1", "password1");
		String newEmail = "newEmail";
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		when(clientRepository.findByEmail(newEmail)).thenReturn(Optional.of(new Client()));
		
		Exception ex = assertThrows(
				IllegalStateException.class, 
				() ->  underTest.updateClient(client.getUsername(), newEmail, null, null)
				);
		
		assertThat(ex.getMessage()).contains("Email", newEmail, "is already taken");
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" ", "\n", "\t", "client1@mail.com"})
	void updateClientEmailIfNotValid(String newEmail) {
		Client client = new Client("client1@mail.com", "client1", "password1");
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		Client updatedClient = underTest.updateClient(client.getUsername(), newEmail, null, null);
		
		assertThat(updatedClient.getEmail()).isEqualTo("client1@mail.com");
		assertThat(updatedClient).isEqualTo(client);
	}
	
	@Test
	void updateClientUsernameIfVaild() {
		Client client = new Client("client1@mail.com", "client1", "password1");
		String newUsername = "newUsername";
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		Client updatedClient = underTest.updateClient(client.getUsername(), null, newUsername, null);
		
		assertThat(updatedClient.getUsername()).isEqualTo(newUsername);
		assertThat(updatedClient).isEqualTo(client);
	}
	
	@Test
	void updateClientUsernameIfExists() {
		Client client = new Client("client1@mail.com", "client1", "password1");
		String newUsername = "newUsername";
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		when(clientRepository.findByUsername(newUsername)).thenReturn(Optional.of(new Client()));
		
		Exception ex = assertThrows(
				IllegalStateException.class, 
				() -> underTest.updateClient(client.getUsername(), null, newUsername, null)
				);
		
		assertThat(ex.getMessage()).contains("Username", newUsername, "is already taken");
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" ", "\n", "\t", "client1"})
	void updateClietnUsernameIfNotValid(String newUsername) {
		Client client = new Client("client1@mail.com", "client1", "password1");
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		Client updatedClient = underTest.updateClient(client.getUsername(), null, newUsername, null);
		
		assertThat(updatedClient.getUsername()).isEqualTo("client1");
		assertThat(updatedClient).isEqualTo(client);
	}
	
	@Test
	void updateClientPasswordIfValid() {
		Client client = new Client("client1@mail.com", "client1", "password1");
		String newPassword = "newPassword";
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
		
		Client updatedClient = underTest.updateClient(client.getUsername(), null, null, newPassword);
		
//		password is not encoded here because of password encoder is mock
		assertThat(updatedClient.getPassword()).isEqualTo(newPassword);
		assertThat(updatedClient).isEqualTo(client);
	}
	
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" ", "\n", "\t"})
	void updateClientPassword_notValid(String newPassword) {
		Client client = new Client("client1@mail.com", "client1", "password1");
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		Client updatedClient = underTest.updateClient(client.getUsername(), null, null, newPassword);
		
//		password is not encoded here because of password encoder is mock
		assertThat(updatedClient.getPassword()).isEqualTo("password1");
		assertThat(updatedClient).isEqualTo(client);
	}
	
	@Test
	void updateClientIfClientNotExists() {
		String clientUsername = "client";
		when(clientRepository.findByUsername(clientUsername)).thenReturn(Optional.empty());
		
		Exception ex = assertThrows(UsernameNotFoundException.class, 
				() -> underTest.updateClient(clientUsername, null, null, null)
				);
		
		assertThat(ex.getMessage()).contains("Username", clientUsername, "not found");
	}
	
}
