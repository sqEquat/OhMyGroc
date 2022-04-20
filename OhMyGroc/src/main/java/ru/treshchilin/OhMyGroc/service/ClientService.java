package ru.treshchilin.OhMyGroc.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.model.Role;
import ru.treshchilin.OhMyGroc.model.ShoppingList;
import ru.treshchilin.OhMyGroc.repo.ClientRepository;
import ru.treshchilin.OhMyGroc.repo.RoleRepository;

@Service
public class ClientService implements UserDetailsService{
	
	private final ClientRepository clientRepository;
	private final RoleRepository roleRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public ClientService(ClientRepository clientRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		super();
		this.clientRepository = clientRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<Client> getClients() {
		return clientRepository.findAll();
	}

	public Client getClient(Long id) {
		return clientRepository.findById(id).orElseThrow();
	}
	
	public Client getClient(String username) {
		return clientRepository.findByUsername(username).orElseThrow();
	}
	
	public Client addNewClient(Client client) {
		if (clientRepository.findByUsername(client.getUsername()).isPresent()) {
			throw new IllegalStateException("Username is already taken");
		}
		
		if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
			throw new IllegalStateException("Email is already taken");
		}
		
		client.setPassword(passwordEncoder.encode(client.getPassword()));
		return clientRepository.save(client);
	}

	public void deleteClient(Long clientId) {
		if (!clientRepository.existsById(clientId)) {
			throw new IllegalStateException("Client with id " + clientId + " does not exists");
		}
		
		clientRepository.deleteById(clientId);
	}

	@Transactional
	public Client updateClient(Long clientId, String username, String email) {
		Client client = clientRepository.findById(clientId)
				.orElseThrow(() -> new IllegalStateException(
				"Client with id " + clientId + " does not exists"));
		
		if (username != null &&
				username.length() > 0 &&
				!Objects.equals(client.getUsername(), username)) {
			client.setUsername(username);
		}
		
		if (email != null &&
				email.length() > 0 &&
				!Objects.equals(client.getEmail(), email)) {
			if(!clientRepository.findByEmail(email).isEmpty()) {
				throw new IllegalStateException("Email is already exists");
			}
			
			client.setEmail(email);
		}
		
		return client;
	}
	
//	Shopping lists
	
	public List<ShoppingList> getShoppingLists(String username) {
		return clientRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"))
				.getShopLists();
	}
	
	public ShoppingList getShoppingListById(String username, Long listId) {
		return clientRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"))
				.getShopLists().stream()
				.filter(list -> list.getId()
				.equals(listId))
				.findAny()
				.orElseThrow(() -> new IllegalStateException("List with id=" + listId + " not found!"));
	}

	@Transactional
	public ShoppingList addNewShoppingList(String username, ShoppingList shoppingList) {
		Client client = clientRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
		
		shoppingList.setDateCreated(LocalDateTime.now());
		shoppingList.setClient(client);
		
		client.addShoppingList(shoppingList);
		
		return shoppingList;
	}
	
	@Transactional
	public ShoppingList updateShoppingList(String username, Long listId, ShoppingList shoppingList) {
		ShoppingList listToUpdate = clientRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"))
				.getShopLists().stream()
				.filter(id -> id.getId()
				.equals(listId))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("List with id=" + listId + " not found!"));
		
		listToUpdate.setItems(shoppingList.getItems());
		
		return listToUpdate;
	}
	
	@Transactional
	public String deleteShoppingList(String username, Long listId) {
		Client client = clientRepository.findByUsername(username).orElseThrow();
		
		if (client.getShopLists().removeIf(list -> list.getId().equals(listId)))
			return "Shopping list with id: " + listId + " was deleted";
		else
			return "There is no shopping list with id: " + listId;
	}
	
//	Roles
	
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}
	
	public Role saveRole(Role role) {
		boolean isRolePresent = roleRepository.findByName(role.getName()).isPresent();
		
		if(isRolePresent) {
			throw new IllegalStateException("Role is already esxists");
		}
		
		return roleRepository.save(role);
	}
	
	public void deleteRole(Long roleId) {
		if (!roleRepository.existsById(roleId)) {
			throw new IllegalStateException("Role not found");
		}
		
		roleRepository.deleteById(roleId);
	}
		
	@Transactional
	public Client addRoleToClient(Long clientId, Long roleId) {
		Optional<Client> clientToBeUpdated = clientRepository.findById(clientId);
		Optional<Role> roleToBeAdded = roleRepository.findById(roleId);
		
		if (clientToBeUpdated.isEmpty()) {
			throw new IllegalStateException("Client not found");
		}
		
		if (roleToBeAdded.isEmpty()) {
			throw new IllegalStateException("Role not found");
		}
		
		Collection<Role> clientRoles = clientToBeUpdated.get().getRoles();
		Role role = roleToBeAdded.get();
		
		if (clientRoles.contains(role)) {
			throw new IllegalStateException("Client already has role: " + role.getName());
		}
		
		clientRoles.add(role);
		
		return clientToBeUpdated.get();
	}
	
	@Transactional
	public Client removeRoleFromClient(Long clientId, Long roleId) {
		Optional<Client> clientToBeUpdated = clientRepository.findById(clientId);
		
		if (clientToBeUpdated.isEmpty()) {
			throw new IllegalStateException("Client not found");
		}
		
		Client client = clientToBeUpdated.get();
		client.getRoles().removeIf(role -> role.getId().equals(roleId));
		
		return client;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Client> clientOp = clientRepository.findByUsername(username);
		
		if (clientOp.isEmpty()) {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
		
		Client client = clientOp.get();
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		client.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		
		return new User(client.getUsername(), client.getPassword(), authorities);
	}

}
