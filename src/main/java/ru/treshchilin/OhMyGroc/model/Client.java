package ru.treshchilin.OhMyGroc.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name = "Client")
@Table(name = "client")
public class Client {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "email",
			unique = true,
			nullable = false)
	private String email;
	
	@Column(name = "username", 
			unique = true,
			nullable = false)
	private String username;
	
	@Column(name = "password",
			nullable = false)
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@Column(name = "role")
	private Collection<Role> roles = new HashSet<>();
	
	@OneToMany(
			mappedBy = "owner",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.LAZY)
	@Column(name = "shoping_lists_id")
	@JsonManagedReference
	private List<ShoppingList> shopLists;
	
	
	public Client() {
	}

	public Client(Long id, String email, String username, String password, List<ShoppingList> shopLists, Collection<Role> roles) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.password = password;
		this.shopLists = shopLists;
		this.roles = roles;
	}
		
	public Client(String email, String username, String password) {
		super();
		this.email = email;
		this.username = username;
		this.password = password;
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
	
	public List<ShoppingList> getShopLists() {
		return shopLists;
	}
	public void setShopLists(List<ShoppingList> shopLists) {
		this.shopLists = shopLists;
	}

	public void addShoppingList(ShoppingList shoppingList) {
		shopLists.add(shoppingList);
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
