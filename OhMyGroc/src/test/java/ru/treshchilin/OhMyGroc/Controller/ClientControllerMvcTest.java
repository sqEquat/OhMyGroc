package ru.treshchilin.OhMyGroc.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.treshchilin.OhMyGroc.Model.Client;
import ru.treshchilin.OhMyGroc.Service.ClientService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ClientController.class)
@Disabled
class ClientControllerMvcTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	private ClientService clientService;
	
	@Test
	void testGetClients() throws Exception {
		List<Client> clients = List.of(
				new Client(1L, "test1@test.com", "Test1", Collections.emptyList()),
				new Client(2L, "test2@test.com", "Test2", Collections.emptyList()),
				new Client(3L, "test3@test.com", "Test3", Collections.emptyList()));
		when(clientService.getClients()).thenReturn(clients);
		
		MvcResult mvcResult = mockMvc.perform(get("/api/v1/client"))
		   .andExpect(status().isOk())
		   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		   .andReturn();
		
		String getResponseResult = mvcResult.getResponse().getContentAsString();
		
		verify(clientService, atMostOnce()).getClients();
		assertThat(getResponseResult).isEqualToIgnoringWhitespace(mapper.writeValueAsString(clients));
		
	}
	
	@Test
	void testRegisterNewClientIfValid() throws Exception {
		Client clinet = new Client("test1@test.com", "Test1");
		
		mockMvc.perform(post("/api/v1/client")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(clinet)))
				.andExpect(status().isOk());
		
		ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
		verify(clientService, atMostOnce()).addNewClient(captor.capture());
		Client capturedClient = captor.getValue();
		
		assertThat(capturedClient.getEmail()).isEqualTo(clinet.getEmail());
		assertThat(capturedClient.getUsername()).isEqualTo(clinet.getUsername());
	}
	
	@Test
	void testRegisterClientWhenEmailIsInvalid() throws Exception {
		Client clinet = new Client("test1test.com", "Test1");
		
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/client")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(clinet)))
				.andExpect(status().isBadRequest())
				.andReturn();

		verify(clientService, never()).addNewClient(any());
		assertThat(mvcResult.getResolvedException().getMessage()).contains("Invalid Email format");
	}
	
	@Test
	void testRegisterClientWhenFiledsAreEmpty() throws Exception {
		Client clinet = new Client();
		
		MvcResult mvcResult = mockMvc.perform(post("/api/v1/client")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(clinet)))
				.andExpect(status().isBadRequest())
				.andReturn();
		
		verify(clientService, never()).addNewClient(any());
		assertThat(mvcResult.getResolvedException().getMessage()).contains("Email shouldn't be empty", "Username shouldn't be empty");
	}
	
	@Test
	void testUpdateClientWithNegativeId() throws Exception {
		assertThrows(NestedServletException.class, () -> {
		mockMvc.perform(put("/api/v1/client/{clientId}", 0)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();
		});
		verify(clientService, never()).updateClient(any(), any(), any());
	}
}
