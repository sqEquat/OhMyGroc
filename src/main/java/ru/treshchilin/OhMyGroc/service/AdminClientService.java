package ru.treshchilin.OhMyGroc.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.treshchilin.OhMyGroc.exception.IdNotFoundException;
import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.model.Role;
import ru.treshchilin.OhMyGroc.repo.ClientRepository;
import ru.treshchilin.OhMyGroc.repo.RoleRepository;

@Service
public class AdminClientService {

	private final ClientRepository clientRepository;
	private final RoleRepository roleRepository;

	@Autowired
	public AdminClientService(ClientRepository clientRepository, RoleRepository roleRepository) {
		this.clientRepository = clientRepository;
		this.roleRepository = roleRepository;		
	}
	
	/*
	 * Get all clients
	 */
	public List<Client> getClients() {
		return clientRepository.findAll();
	}
	
	/*
	 * Get client with id if exists
	 */
	public Client getClientById(Long id) {
		return clientRepository.findById(id).orElseThrow(() -> new IdNotFoundException("Client with id= " + id + " not found"));
	}
	
	/*
	 * Delete client by id if exists
	 */
	public void deleteClient(Long clientId) {
		if (!clientRepository.existsById(clientId)) {
			throw new IdNotFoundException("Client with id= " + clientId + " not found");
		}
		
		clientRepository.deleteById(clientId);
	}
	
	/*
	 * Get list of all roles
	 */
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}
	
	/*
	 * Add new role 
	 */
	public Role saveRole(Role role) {		
		if(roleRepository.findByName(role.getName()).isPresent()) {
			throw new IllegalStateException("Role already esxists");
		}
		
		return roleRepository.save(role);
	}
	
	/*
	 * Delete role by id if exists
	 */
	public void deleteRole(Long roleId) {
		if (!roleRepository.existsById(roleId)) {
			throw new IdNotFoundException("Role with id= " + roleId + " not found");
		}
		
		roleRepository.deleteById(roleId);
	}
	
	/*
	 * Add role with roleId to the client with clientId
	 */
	@Transactional
	public Client addRoleToClient(Long clientId, Long roleId) {
		Optional<Client> clientToBeUpdated = clientRepository.findById(clientId);
		Optional<Role> roleToBeAdded = roleRepository.findById(roleId);
		
		if (clientToBeUpdated.isEmpty()) {
			throw new IdNotFoundException("Client with id= " + clientId + " not found");
		}
		
		if (roleToBeAdded.isEmpty()) {
			throw new IdNotFoundException("Role with id= " + roleId + " not found");
		}
		
		Collection<Role> clientRoles = clientToBeUpdated.get().getRoles();
		Role role = roleToBeAdded.get();
		
		if (clientRoles.contains(role)) {
			throw new IllegalStateException("Client already has role: " + role.getName());
		}
		
		clientRoles.add(role);
		
		return clientToBeUpdated.get();
	}
	
	/*
	 * Remove role with roleId from roles in client with clientId
	 */
	@Transactional
	public Client removeRoleFromClient(Long clientId, Long roleId) {
		Optional<Client> clientToBeUpdated = clientRepository.findById(clientId);
		
		if (clientToBeUpdated.isEmpty()) {
			throw new IdNotFoundException("Client with id= " + clientId + " not found");
		}
		
		Client client = clientToBeUpdated.get();
		client.getRoles().removeIf(role -> role.getId().equals(roleId));
		
		return client;
	}
	
}
