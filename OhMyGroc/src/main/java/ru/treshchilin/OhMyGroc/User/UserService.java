package ru.treshchilin.OhMyGroc.User;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	public List<User> getUsers() {
		return List.of(
				new User(1L, "equat@mail.com", "Equat", List.of("Milk", "Tomatoes", "Tea", "Bread", "Epica")));
	}
}
