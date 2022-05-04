package ru.treshchilin.OhMyGroc.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ClientRegisterDto {

	@NotBlank
	@Email(message = "Invalid email format")
	private String email;
	
	@NotBlank(message = "Username couldn't be empty")
	private String username;
	
	@NotBlank
	@Size(min = 6, message = "Password must be at least 6 symbols")
	private String password;
	
	public ClientRegisterDto(String email, String username, String password) {
		super();
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
