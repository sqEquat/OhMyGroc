package ru.treshchilin.OhMyGroc.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.treshchilin.OhMyGroc.model.ShoppingList;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long>{

}
