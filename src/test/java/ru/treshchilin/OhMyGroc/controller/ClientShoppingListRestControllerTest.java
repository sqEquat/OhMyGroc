package ru.treshchilin.OhMyGroc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ru.treshchilin.OhMyGroc.model.ShoppingList;
import ru.treshchilin.OhMyGroc.service.ClientShoppingListService;

@ExtendWith(MockitoExtension.class)
class ClientShoppingListRestControllerTest {

	@Mock
	private ClientShoppingListService listService;
	private static Principal principal;
	
	@InjectMocks
	private ClientShoppingListRestController underTest;
	
	@BeforeAll
	static void principalInit() {
		String username = "client";
		principal = mock(Principal.class);
		when(principal.getName()).thenReturn(username);
	}
	
	@Test
	void getShoppingList_returnOkAndShoppingLists_ifPrincipalExists() {
		List<ShoppingList> shoppingLists = List.of(new ShoppingList(), new ShoppingList());
		when(listService.getShoppingLists(anyString())).thenReturn(shoppingLists);
		
		ResponseEntity<List<ShoppingList>> actualResponse = underTest.getShoppingLists(principal);
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(shoppingLists);
	}
	
	@Test
	void getShoppingList_returnOkAndShoppingList_ifPrincipalAndListIdExists() {
		Long listId = 1L;
		ShoppingList shoppingList = new ShoppingList(listId, null, null);
		when(listService.getShoppingListById(anyString(), eq(shoppingList.getId()))).thenReturn(shoppingList);
		
		ResponseEntity<ShoppingList> actualResponse = underTest.getShoppingList(principal, listId);
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(shoppingList);
	}
	
	@Test
	void addNewShoppingList_returnOkAndShoppingList_ifPrincipalAndListExists() {
		ShoppingList shoppingList = new ShoppingList();
		when(listService.addNewShoppingList(anyString(), eq(shoppingList))).thenReturn(shoppingList);
		
		ResponseEntity<ShoppingList> actualResponse = underTest.addNewShoppingList(principal, shoppingList);
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(shoppingList);
	}
	
	@Test
	void updateShoppingList_returnOkAndShoppingList_ifPrincipalAndIdAndListExists() {
		Long listId = 1L;
		ShoppingList newItems = new ShoppingList(null, null, List.of());
		ShoppingList updatedList = new ShoppingList(listId, null, newItems.getItems());
		when(listService.updateShoppingList(anyString(), eq(listId), eq(newItems))).thenReturn(updatedList);
		
		ResponseEntity<ShoppingList> actualResponse = underTest.updateShoppingList(principal, listId, newItems);
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(updatedList);
	}
	
	@Test
	void deleteShoppingList_returnSuccessfulMessage_ifPrincipalAndIdExists() {
		Long listId = 1L;
		String message = "Shopping list with id: " + listId + " was deleted";
		when(listService.deleteShoppingList(anyString(), eq(listId))).thenReturn(message);
		
		ResponseEntity<?> actualResponse = underTest.deleteShoppingList(principal, listId);
		
		assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(actualResponse.getBody()).isEqualTo(message);
	}

}
