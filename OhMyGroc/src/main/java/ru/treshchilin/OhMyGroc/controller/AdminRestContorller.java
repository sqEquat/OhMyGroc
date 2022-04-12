package ru.treshchilin.OhMyGroc.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.model.Role;
import ru.treshchilin.OhMyGroc.service.ClientService;

@RestController
@RequestMapping("/api/v2/admin")
public class AdminRestContorller {

	private final ClientService clientService;

	@Autowired
	public AdminRestContorller(ClientService clientService) {
		this.clientService = clientService;
	}
	
	// Clients
	
	@GetMapping("/clients")
	public ResponseEntity<List<Client>> getClients() {
		return ResponseEntity.ok().body(clientService.getClients());
	}
	
	@GetMapping("/clients/{clientId}")
	public ResponseEntity<Client> getClient(
			@PathVariable(name = "clietnId", required = true) Long clientId) {
		return ResponseEntity.ok().body(clientService.getClient(clientId));
	}
	
	@PostMapping("/clients")
	public ResponseEntity<Client> saveClient(@RequestBody Client client) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v2/admin/clients").toUriString());
		return ResponseEntity.created(uri).body(clientService.addNewClient(client));
	}
	
	@PutMapping("/clients/{clientId}/roles/add/{roleId}")
	public ResponseEntity<Client> addRoleToClient(
			@PathVariable(name = "clientId", required = true) Long clientId,
			@PathVariable(name = "roleId", required = true) Long roleId) {
		return ResponseEntity.ok().body(clientService.addRoleToClient(clientId, roleId));
	}
	
	@PutMapping("/clients/{clientId}/roles/remove/{roleId}")
	public ResponseEntity<Client> removeRoleFromClient(
			@PathVariable(name = "clientId", required = true) Long clientId,
			@PathVariable(name = "roleId", required = true) Long roleId) {
		return ResponseEntity.ok().body(clientService.removeRoleFromClient(clientId, roleId));
	}
		
	// Roles
	
	@GetMapping("/roles")
	public ResponseEntity<List<Role>> getRoles(){
		return ResponseEntity.ok().body(clientService.getRoles());
	}
	
	@PostMapping("/roles")
	public ResponseEntity<Role> saveRole(@RequestBody Role role){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v2/admin/role").toUriString());
		return ResponseEntity.created(uri).body(clientService.saveRole(role));
	}
	
	@DeleteMapping("/roles/{id}")
	public ResponseEntity<?> deleteRole(
			@PathVariable(name = "id", required = true) Long roleId) {
		clientService.deleteRole(roleId);
		return ResponseEntity.ok().body("Role deleted");
	}
}
