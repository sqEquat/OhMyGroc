package ru.treshchilin.OhMyGroc.Client;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

	@Query("SELECT c FROM Client c WHERE c.email = ?1")
	List<Client> findByEmail(String email);
}
