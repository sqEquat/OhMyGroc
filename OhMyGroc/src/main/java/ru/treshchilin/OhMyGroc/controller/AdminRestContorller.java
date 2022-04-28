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
import ru.treshchilin.OhMyGroc.service.AdminClientService;

@RestController
@RequestMapping("/api/v2/admin")
public class AdminRestContorller {

	private final AdminClientService adminService;

	@Autowired
	public AdminRestContorller(AdminClientService adminService) {
		this.adminService = adminService;
	}
	
	// Clients
	
	@GetMapping("/clients")
	public ResponseEntity<List<Client>> getClients() {
		return ResponseEntity.ok().body(adminService.getClients());
	}
	
	@GetMapping("/clients/{clientId}")
	public ResponseEntity<Client> getClient(
			@PathVariable(name = "clietnId", required = true) Long clientId) {
		return ResponseEntity.ok().body(adminService.getClientById(clientId));
	}
	
	@DeleteMapping("/clients/{clientId}")
	public ResponseEntity<?> deleteClient(
			@PathVariable(name = "clientId", required = true) Long clientId) {
		adminService.deleteClient(clientId);
		return ResponseEntity.ok().build();
	}
	
//	@PostMapping("/clients")
//	public ResponseEntity<Client> saveClient(@RequestBody Client client) {
//		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v2/admin/clients").toUriString());
//		return ResponseEntity.created(uri).body(clientService.addNewClient(client));
//	}
	
//	Roles
	
	@PutMapping("/clients/{clientId}/roles/add/{roleId}")
	public ResponseEntity<Client> addRoleToClient(
			@PathVariable(name = "clientId", required = true) Long clientId,
			@PathVariable(name = "roleId", required = true) Long roleId) {
		return ResponseEntity.ok().body(adminService.addRoleToClient(clientId, roleId));
	}
	
	@PutMapping("/clients/{clientId}/roles/remove/{roleId}")
	public ResponseEntity<Client> removeRoleFromClient(
			@PathVariable(name = "clientId", required = true) Long clientId,
			@PathVariable(name = "roleId", required = true) Long roleId) {
		return ResponseEntity.ok().body(adminService.removeRoleFromClient(clientId, roleId));
	}
		
	// Roles
	
	@GetMapping("/roles")
	public ResponseEntity<List<Role>> getRoles(){
		return ResponseEntity.ok().body(adminService.getRoles());
	}
	
	@PostMapping("/roles")
	public ResponseEntity<Role> saveRole(@RequestBody Role role){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v2/admin/role").toUriString());
		return ResponseEntity.created(uri).body(adminService.saveRole(role));
	}
	
	@DeleteMapping("/roles/{id}")
	public ResponseEntity<?> deleteRole(
			@PathVariable(name = "id", required = true) Long roleId) {
		adminService.deleteRole(roleId);
		return ResponseEntity.ok().body("Role deleted");
	}
}
