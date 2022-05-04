package ru.treshchilin.OhMyGroc.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.treshchilin.OhMyGroc.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

	Optional<Role> findByName(String name);
}
