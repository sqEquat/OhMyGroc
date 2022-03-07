package ru.treshchilin.OhMyGroc.Client;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table
public class Client {
	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY
	)
	private Long id;
	@NotBlank(message = "Email shouldn't be empty")
	@Email
	private String email;
	@NotBlank(message = "Username shouldn't be empty")
	private String username;
	@ElementCollection
	private List<String> shopList;
	
	
	public Client() {
	}
	
	public Client(String email, String username) {
		this.email = email;
		this.username = username;
	}

	public Client(String email, String username, List<String> shopLists) {
		this.email = email;
		this.username = username;
		this.shopList = shopLists;
	}

	public Client(Long id, String email, String username, List<String> shopLists) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.shopList = shopLists;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	public List<String> getShopLists() {
		return shopList;
	}
	public void setShopLists(List<String> shopLists) {
		this.shopList = shopLists;
	}
	
	@Override
	public String toString() {
		return "Client [id=" + id + ", email=" + email + ", username=" + username + ", shopList=" + shopList + "]";
	}

}
