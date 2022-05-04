package ru.treshchilin.OhMyGroc.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name = "ShoppingList")
@Table(name = "shopping_list")
public class ShoppingList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL)
	@JoinColumn(
			name = "client_fk",
			nullable = false)
	@JsonBackReference
	private Client owner;
	
	@Column(name = "date_created")
	private LocalDateTime dateCreated;
	
	@ElementCollection
	private List<String> items;
	
	
	public ShoppingList() {
	}

	public ShoppingList(Long id, LocalDateTime dateCreated, List<String> items) {
		this.id = id;
		this.dateCreated = dateCreated;
		this.items = items;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Client getOwner() {
		return owner;
	}

	public void setOwner(Client owner) {
		this.owner = owner;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

}
