package ru.treshchilin.OhMyGroc.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.treshchilin.OhMyGroc.User.User;

@RestController
public class FirstRestController {
	
	@GetMapping
	public List<User> hello() {
		return List.of(
				new User(1L, "user@mail.com", "User", List.of("Milk", "Tomatoes", "Tea", "Bread")));
	}
}
