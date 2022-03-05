package ru.treshchilin.OhMyGroc.Client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
public class ClientController {

	private final ClientService userService;
	
	@Autowired
	public ClientController(ClientService userService) {
		this.userService = userService;
	}
	
	@GetMapping
	public List<Client> getUsers() {
		return userService.getUsers();
	}

}
