package ru.treshchilin.OhMyGroc.User;

import java.util.List;

public class User {
	private Long id;
	private String email;
	private String username;
	private List<String> shopList;
	
	
	public User() {
	}
	
	public User(String email, String username, List<String> shopLists) {
		this.email = email;
		this.username = username;
		this.shopList = shopLists;
	}

	public User(Long id, String email, String username, List<String> shopLists) {
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

}
