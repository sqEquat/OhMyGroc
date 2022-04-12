package ru.treshchilin.OhMyGroc.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.treshchilin.OhMyGroc.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

	@Query("SELECT c FROM Client c WHERE c.email = ?1")
	Optional<Client> findByEmail(String email);
	
	Optional<Client> findByUsername(String username);
}
