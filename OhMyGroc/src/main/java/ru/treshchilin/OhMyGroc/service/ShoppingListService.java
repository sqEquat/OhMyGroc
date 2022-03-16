package ru.treshchilin.OhMyGroc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.treshchilin.OhMyGroc.repo.ShoppingListRepository;

@Service
public class ShoppingListService {

	private final ShoppingListRepository listRepository;

	@Autowired
	public ShoppingListService(ShoppingListRepository listRepository) {
		super();
		this.listRepository = listRepository;
	}
	
	
}
