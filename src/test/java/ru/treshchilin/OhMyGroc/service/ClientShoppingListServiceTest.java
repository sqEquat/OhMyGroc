package ru.treshchilin.OhMyGroc.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.model.ShoppingList;
import ru.treshchilin.OhMyGroc.repo.ClientRepository;

@ExtendWith(MockitoExtension.class)
public class ClientShoppingListServiceTest {

	@Mock
	ClientRepository clientRepository;
	
	@InjectMocks
	ClientShoppingListService underTest;
	
//	getShoppingLists
	
	@Test
	void getShoppingLists() {
		List<ShoppingList> shoppingLists = List.of(
				new ShoppingList(1L, null, null),
				new ShoppingList(2L, null, null),
				new ShoppingList(3L, null, null)
				);
		Client client = new Client(1l, "client@mail.com", "client", "password", shoppingLists, List.of());
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		List<ShoppingList> gotList = underTest.getShoppingLists(client.getUsername());
		
		verify(clientRepository, atMostOnce()).findByUsername(client.getUsername());
		assertThat(gotList).isEqualTo(shoppingLists);
	}
	
	@Test
	void getShoppingListsIfUsernameNotFound() {
		when(clientRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		
		Exception ex = assertThrows(
				UsernameNotFoundException.class, 
				() -> underTest.getShoppingLists(anyString())
				);
		
		verify(clientRepository, atMostOnce()).findByUsername(anyString());
		assertThat(ex.getMessage()).contains("Username", "not found");
	}
	
//	getShoppingListById
	
	@ParameterizedTest
	@ValueSource(longs = {1L, 2L, 3L})
	void getShoppingListById(Long listId) {
		List<ShoppingList> shoppingLists = List.of(
				new ShoppingList(1L, null, null),
				new ShoppingList(2L, null, null),
				new ShoppingList(3L, null, null)
				);
		Client client = new Client(1l, "client@mail.com", "client", "password", shoppingLists, List.of());
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		ShoppingList gotList = underTest.getShoppingListById(client.getUsername(), listId);
		
		verify(clientRepository, atMostOnce()).findByUsername(client.getUsername());
		assertThat(gotList.getId()).isEqualTo(listId);
	}
	
	@Test
	void getShoppingListByIdIfUsernameNotFound() {
		when(clientRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		
		Exception ex = assertThrows(
				UsernameNotFoundException.class, 
				() -> underTest.getShoppingListById(anyString(), 1L)
				);
		
		verify(clientRepository, atMostOnce()).findByUsername(anyString());
		assertThat(ex.getMessage()).contains("Username", "not found");
	}
	
	@Test
	void getShoppingListByIdIfIdNotFound() {
		Long listId = 4L;
		List<ShoppingList> shoppingLists = List.of(
				new ShoppingList(1L, null, null),
				new ShoppingList(2L, null, null),
				new ShoppingList(3L, null, null)
				);
		Client client = new Client(1l, "client@mail.com", "client", "password", shoppingLists, List.of());
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		Exception ex = assertThrows(
				IllegalStateException.class,
				() -> underTest.getShoppingListById(client.getUsername(), listId)
				);
		
		verify(clientRepository, atMostOnce()).findByUsername(client.getUsername());
		assertThat(ex.getMessage()).contains("List with id=", listId.toString(), "not found");
	}
	
//	addNewShoppingList
	
	@Test
	void addNewShoppingList() {
		Client client = new Client(1l, "client@mail.com", "client", "password", new ArrayList<>(), List.of());
		ShoppingList shoppingList = new ShoppingList(1L, null, null);
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		ShoppingList gotShoppingList = underTest.addNewShoppingList(client.getUsername(), shoppingList);
		
		verify(clientRepository, atMostOnce()).findByUsername(client.getUsername());
		assertThat(gotShoppingList.getId()).isEqualTo(shoppingList.getId());
		assertThat(client.getShopLists().size()).isEqualTo(1);
	}
	
	@Test
	void addNewShoppingListIfUsernameNotFound() {
		when(clientRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		
		Exception ex = assertThrows(
				UsernameNotFoundException.class,
				() -> underTest.addNewShoppingList(anyString(), new ShoppingList())
				);
		
		verify(clientRepository, atMostOnce()).findByUsername(anyString());
		assertThat(ex.getMessage()).contains("Username", "not found");
	}
	
//	updateShoppingList
	
	@Test
	void updateShoppingList() {
		Long listId = 1L;
		Client client = new Client(1L, "client@mail.com", "client", "password", List.of(new ShoppingList(listId, null, null)), List.of());
		ShoppingList shoppingList = new ShoppingList(null, null, List.of("Milk", "Bread", "Water"));
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		ShoppingList gotList = underTest.updateShoppingList(client.getUsername(), listId, shoppingList);
		
		verify(clientRepository, atMostOnce()).findByUsername(client.getUsername());
		assertThat(gotList.getItems()).isEqualTo(shoppingList.getItems());
	}
	
	@Test
	void updateShoppingListIfUsernameNotFound() {
		when(clientRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		
		Exception ex = assertThrows(
				UsernameNotFoundException.class, 
				() -> underTest.updateShoppingList(anyString(), 0L, new ShoppingList())
				);
		
		verify(clientRepository, atMostOnce()).findByUsername(anyString());
		assertThat(ex.getMessage()).contains("Username", "not found");
	}
	
	@Test
	void updateShoppingListIfIdNotFound() {
		Long listId = 4L;
		List<ShoppingList> shoppingLists = List.of(
				new ShoppingList(1L, null, null),
				new ShoppingList(2L, null, null),
				new ShoppingList(3L, null, null)
				);
		Client client = new Client(1l, "client@mail.com", "client", "password", shoppingLists, List.of());
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		Exception ex = assertThrows(
				IllegalStateException.class,
				() -> underTest.getShoppingListById(client.getUsername(), listId)
				);
		
		verify(clientRepository, atMostOnce()).findByUsername(client.getUsername());
		assertThat(ex.getMessage()).contains("List with id=", listId.toString(), "not found");
	}
	
//	deleteShoppingList
	
	@Test
	void deleteShoppingList() {
		Long listId = 1L;
		List<ShoppingList> shoppingLists = new ArrayList<>(List.of(new ShoppingList(listId, null, null)));
		Client client = new Client(1L, "client@mail.com", "client", "password", shoppingLists, List.of());
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		String result = underTest.deleteShoppingList(client.getUsername(), listId);
		
		verify(clientRepository, atMostOnce()).findByUsername(client.getUsername());
		assertThat(result).contains("Shopping list with id: " + listId + " was deleted");
		assertThat(client.getShopLists().size()).isEqualTo(0);
	}
	
	@Test
	void deleteShoppingListIfUsernameNotFound() {
		when(clientRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		
		Exception ex = assertThrows(
				UsernameNotFoundException.class, 
				() -> underTest.deleteShoppingList(anyString(), 0L)
				);
		
		verify(clientRepository, atMostOnce()).findByUsername(anyString());
		assertThat(ex.getMessage()).contains("Username", "not found");
	}
	
	@Test
	void deleteShoppingListIfIdNotFound() {
		Long listId = 1L;
		List<ShoppingList> shoppingLists = new ArrayList<>(List.of(new ShoppingList(listId + 1L, null, null)));
		Client client = new Client(1L, "client@mail.com", "client", "password", shoppingLists, List.of());
		
		when(clientRepository.findByUsername(client.getUsername())).thenReturn(Optional.of(client));
		
		Exception ex = assertThrows(
				IllegalStateException.class,
				() -> underTest.deleteShoppingList(client.getUsername(), listId)
				);
		
		verify(clientRepository, atMostOnce()).findByUsername(client.getUsername());
		assertThat(ex.getMessage()).contains("There is no shopping list with id: ", listId.toString());
	}
}
