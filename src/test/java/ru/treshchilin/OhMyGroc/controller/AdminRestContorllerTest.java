package ru.treshchilin.OhMyGroc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.model.Role;
import ru.treshchilin.OhMyGroc.service.AdminClientService;

@ExtendWith(MockitoExtension.class)
class AdminRestContorllerTest {

	@Mock
	AdminClientService adminClientService;
	
	@InjectMocks
	AdminRestContorller underTest;
	
	@Test
	void getClients_returnOkAndListOfClients() {
		List<Client> clientsList = List.of(new Client(), new Client(), new Client());
		when(adminClientService.getClients()).thenReturn(clientsList);
		
		ResponseEntity<List<Client>> actualResponse = underTest.getClients();
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(clientsList);
	}
	
	@Test
	void getClient_returnOkAndClient_ifIdIsValid() {
		Client client = new Client();
		when(adminClientService.getClientById(anyLong())).thenReturn(client);
		
		ResponseEntity<Client> actualResponse = underTest.getClient(anyLong());
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(client);
	}
	
	@Test
	void deleteClient_returnOk_ifIdIsValid() {
		ResponseEntity<?> actualResponse = underTest.deleteClient(anyLong());
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	void addRoleToClient_returnOkAndClient_ifClienIdAndRoleIdAreValid() {
		Long clientId = 1L;
		Long roleId = 1L;
		Client client = new Client(clientId, null, null, null, null, Set.of(new Role(roleId, null)));
		when(adminClientService.addRoleToClient(clientId, roleId)).thenReturn(client);
		
		ResponseEntity<Client> actualResponse = underTest.addRoleToClient(clientId, roleId);
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(client);
	}
	
	@Test
	void removeRoleFromClient_returnOkAndClient_ifClientIdAndRoleIdAreValid() {
		Long clientId = 1L;
		Long roleId = 1L;
		Client client = new Client();
		when(adminClientService.removeRoleFromClient(clientId, roleId)).thenReturn(client);
		
		ResponseEntity<Client> actualResponse = underTest.removeRoleFromClient(clientId, roleId);
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(client);
	}
	
	@Test
	void getRoles_returnOkAndListOfRoles() {
		List<Role> rolesList = List.of(new Role(), new Role());
		when(adminClientService.getRoles()).thenReturn(rolesList);
		
		ResponseEntity<List<Role>> actualResponse = underTest.getRoles();
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(rolesList);
	}
	
	@Test
	void saveRole_returnCreatedAndLocationAndRole_ifRoleNotExists() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		Role role = new Role();
		when(adminClientService.saveRole(role)).thenReturn(role);
		
		ResponseEntity<Role> actualResponse = underTest.saveRole(role);
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(actualResponse.getHeaders().getLocation().getPath()).isEqualTo("/api/v2/admin/roles");
		assertThat(actualResponse.getBody()).isEqualTo(role);
	}
	
	@Test
	void deleteRole_returnOkAndMessage_ifRoleIdIsValid() {
		Long roleId = 1L;
		
		ResponseEntity<?> actualResponse = underTest.deleteRole(roleId);
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo("Role deleted");
	}
}
