package ru.treshchilin.OhMyGroc.User;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
public class UserController {
	
	@GetMapping
	public List<User> hello() {
		return List.of(
				new User(1L, "user@mail.com", "User", List.of("Milk", "Tomatoes", "Tea", "Bread")));
	}

}
