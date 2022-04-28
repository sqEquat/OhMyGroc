package ru.treshchilin.OhMyGroc.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public List<Client> getClients() {
		return clientRepository.findAll();
	}
	
	public Client getClientById(Long id) {
		return clientRepository.findById(id).orElseThrow(() -> new IllegalStateException("No client with id: " + id));
	}
	
	public void deleteClient(Long clientId) {
		if (!clientRepository.existsById(clientId)) {
			throw new IllegalStateException("Client with id " + clientId + " does not exists");
		}
		
		clientRepository.deleteById(clientId);
	}
	
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
	
}
