package ru.treshchilin.OhMyGroc.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.treshchilin.OhMyGroc.exception.IdNotFoundException;
import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.model.Role;
import ru.treshchilin.OhMyGroc.repo.ClientRepository;
import ru.treshchilin.OhMyGroc.repo.RoleRepository;

@ExtendWith(MockitoExtension.class)
public class AdminClientServiceTest {

	@Mock
	ClientRepository clientRepository;
	
	@Mock
	RoleRepository roleRepository;
	
	@InjectMocks
	AdminClientService underTest;
	
//	getClients
	
	@Test
	void getClientsTest() {
		List<Client> clientsList = List.of(
				new Client(1L, "admin@test.com", "admin", "password", List.of(), List.of(new Role(1L, "ROLE_ADMIN"))),
				new Client(2L, "client@test.com", "client", "password", List.of(), List.of(new Role(2L, "ROLE_CLIENT")))
				);
		
		when(clientRepository.findAll()).thenReturn(clientsList);
		
		List<Client> gotClients = underTest.getClients();
		
		verify(clientRepository, atMostOnce()).findAll();
		assertThat(gotClients).containsAll(clientsList);
	}
	
//	getClientById
	
	@Test
	void getClientByIdifExists() {
		Client client = new Client(2L, "client@test.com", "client", "password", List.of(), List.of());
		
		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		
		Client gotClient = underTest.getClientById(client.getId());
		
		verify(clientRepository, atMostOnce()).findById(client.getId());
		assertThat(gotClient.getId()).isEqualTo(client.getId());
	}
	
	@Test 
	void getClientByIdIfNotExists() {
		when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		Exception ex = assertThrows(
				IdNotFoundException.class,
				() -> underTest.getClientById(anyLong())
				);
		
		verify(clientRepository, atMostOnce()).findById(anyLong());
		assertThat(ex.getMessage()).contains("Client with id", "not found");
	}
	
//	deleteClient
	
	@Test
	void deleteClientIfIdExists() {
		Long id = 1L;
		
		when(clientRepository.existsById(id)).thenReturn(true);
		
		underTest.deleteClient(id);
		
		verify(clientRepository, atMostOnce()).deleteById(id);
	}
	
	@Test
	void deleteClientIfIdNotExists() {
		when(clientRepository.existsById(anyLong())).thenReturn(false);
		
		Exception ex = assertThrows(
				IdNotFoundException.class,
				() -> underTest.deleteClient(anyLong())
				);
		
		verify(clientRepository, never()).deleteById(anyLong());
		assertThat(ex.getMessage()).contains("Client with id", "not found");
	}
	
//	getRoles
	
	@Test
	void getRoles() {
		List<Role> roles = List.of(
				new Role(1L, "ROLE_ADMIN"),
				new Role(2L, "ROLE_CLIENT")
				);
		
		when(roleRepository.findAll()).thenReturn(roles);
		
		List<Role> gotRoles = underTest.getRoles();
		
		verify(roleRepository, atMostOnce()).findAll();
		assertThat(gotRoles).containsAll(roles);
	}
	
//	saveRole
	
	@Test
	void saveRoleIfNotExists() {
		Role roleToAdd = new Role(null, "ROLE_GUEST");
		ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
		
		when(roleRepository.findByName(roleToAdd.getName())).thenReturn(Optional.empty());
		
		underTest.saveRole(roleToAdd);
		
		verify(roleRepository, atMostOnce()).save(roleArgumentCaptor.capture());
		assertThat(roleArgumentCaptor.getValue().getName()).isEqualTo(roleToAdd.getName());
	}
	
	@Test
	void saveRoleIfExists() {
		when(roleRepository.findByName(anyString())).thenReturn(Optional.of(new Role()));
		
		Exception ex = assertThrows(
				IllegalStateException.class,
				() -> underTest.saveRole(new Role(null, "ROLE_ADMIN"))
				);
		
		verify(roleRepository, never()).save(any());
		assertThat(ex.getMessage()).contains("Role already esxists");
	}
	
//	deleteRole
	
	@Test
	void deleteRoleIfExists() {
		Long roleId = 2L;
		ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
		
		when(roleRepository.existsById(anyLong())).thenReturn(true);
		
		underTest.deleteRole(roleId);
		
		verify(roleRepository, atMostOnce()).deleteById(longArgumentCaptor.capture());
		assertThat(longArgumentCaptor.getValue()).isEqualTo(roleId);		
	}
	
	@Test
	void deleteRoleIfIdNotFound() {
		Long roleId = 2L;
		
		when(roleRepository.existsById(roleId)).thenReturn(false);
		
		Exception ex = assertThrows(
				IdNotFoundException.class,
				() -> underTest.deleteRole(roleId)
				);
		
		verify(roleRepository, never()).save(any());
		assertThat(ex.getMessage()).contains("Role with id", "not found");
	}
	
//	addRoleToClient
	
	@Test
	void addRoleToClient() {
		Role role = new Role(1L, "ROLE_ADMIN");
		Client client = new Client(
				2L, 
				"client@mail.com", 
				"client", 
				"password", 
				List.of(), 
				new ArrayList<Role>(List.of(new Role(2L, "ROLE_CLIENT")))
				);
		
		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
		
		Client gotClient = underTest.addRoleToClient(client.getId(), role.getId());
		
		assertThat(gotClient.getRoles()).contains(role);
	}
	
	@Test
	void addRoleToClientIfClientIdNotExists() {
		Role role = new Role(1L, "ROLE_ADMIN");
		
		when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());
		when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
		
		Exception ex = assertThrows(
				IdNotFoundException.class,
				() -> underTest.addRoleToClient(anyLong(), role.getId())
				);
		
		assertThat(ex.getMessage()).contains("Client with id", "not found");
	}
	
	@Test
	void addRoleToClientIfRoleIdNotExists() {
		Client client = new Client(2L, "client@mail.com", "client", "password", List.of(), List.of());
		
		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		Exception ex = assertThrows(
				IdNotFoundException.class,
				() -> underTest.addRoleToClient(client.getId(), anyLong())
				);
		
		assertThat(ex.getMessage()).contains("Role with id", "not found");
	}
	
	@Test
	void addRoleToClientIfClientHasRole() {
		Role role = new Role(2L, "ROLE_ADMIN");
		Client client = new Client(
				2L, 
				"client@mail.com", 
				"client", 
				"password", 
				List.of(), 
				new ArrayList<Role>(List.of(role))
				);
		
		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
		
		Exception ex = assertThrows(
				IllegalStateException.class,
				() -> underTest.addRoleToClient(client.getId(), role.getId())
				);
		
		assertThat(ex.getMessage()).contains("Client already has role:", role.getName());
	}
	
//	removeRoleFromClient
	
	@Test
	void removeRoleFromClient() {
		Role role = new Role(2L, "ROLE_ADMIN");
		Client client = new Client(
				2L, 
				"client@mail.com", 
				"client", 
				"password", 
				List.of(), 
				new ArrayList<Role>(List.of(role))
				);
		
		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		
		Client gotClient = underTest.removeRoleFromClient(client.getId(), role.getId());
		
		assertThat(gotClient.getRoles()).doesNotContain(role);
	}
	
	@Test
	void removeRoleFromClientIfClientNotExists() {
		Role role = new Role(2L, "ROLE_ADMIN");
		
		when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		Exception ex = assertThrows(
				IdNotFoundException.class,
				() -> underTest.removeRoleFromClient(anyLong(), role.getId())
				);
		
		assertThat(ex.getMessage()).contains("Client with id", "not found");
	}
	
	@Test
	void removeRoleFromClientIfClientHasNoRole() {
		Role role = new Role(2L, "ROLE_ADMIN");
		Client client = new Client(
				2L, 
				"client@mail.com", 
				"client", 
				"password", 
				List.of(), 
				new ArrayList<Role>(List.of(new Role(1L, "ROLE_CLIENT")))
				);
		
		when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));
		
		Client gotClient = underTest.removeRoleFromClient(client.getId(), role.getId());
		
		assertThat(gotClient.getRoles().size()).isEqualTo(1);
	}
}
