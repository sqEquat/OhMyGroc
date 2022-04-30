package ru.treshchilin.OhMyGroc.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.treshchilin.OhMyGroc.dto.ClientRegisterDto;
import ru.treshchilin.OhMyGroc.model.Client;
import ru.treshchilin.OhMyGroc.repo.ClientRepository;
import ru.treshchilin.OhMyGroc.repo.RoleRepository;

@Service
public class ClientService implements UserDetailsService{
	
	private final ClientRepository clientRepository;
	private final RoleRepository roleRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public ClientService(ClientRepository clientRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.clientRepository = clientRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Client client = clientRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
				
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		client.getRoles().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});
		
		return new User(client.getUsername(), client.getPassword(), authorities);
	}
	
	public Client getClient(String username) {
		return clientRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
	}
	
	public Client addNewClient(ClientRegisterDto clientRegisterDto) {
		if (clientRepository.findByUsername(clientRegisterDto.getUsername()).isPresent()) {
			throw new IllegalStateException("Username is already taken");
		}
		
		if (clientRepository.findByEmail(clientRegisterDto.getEmail()).isPresent()) {
			throw new IllegalStateException("Email is already taken");
		}
		
		Client registrationClient = new Client(clientRegisterDto.getEmail(), 
				clientRegisterDto.getUsername(), 
				passwordEncoder.encode(clientRegisterDto.getPassword()));
		
		registrationClient.getRoles().add(roleRepository.findByName("ROLE_CLIENT")
				.orElseThrow(() -> new IllegalStateException("There is no role: ROLE_CLIENT")));
		
		return clientRepository.save(registrationClient);
	}
	
	@Transactional
	public Client updateClient(String currentUername, String newEmail, String newUsername, String newPassword) {
		Client client = clientRepository.findByUsername(currentUername)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + currentUername + " not found"));

		if (
				newEmail != null &&
				!newEmail.isBlank() &&
				!client.getEmail().equals(newEmail) ) {
			if (clientRepository.findByEmail(newEmail).isPresent()) {
				throw new IllegalStateException("Email " + newEmail + "is already taken");
			}
			
			client.setEmail(newEmail);
		}
		
		if (
				newUsername != null &&
				!newUsername.isBlank() &&
				!client.getUsername().equals(newUsername) ) {
			if (clientRepository.findByUsername(newUsername).isPresent()) {
				throw new IllegalStateException("Username " + newUsername + "is already taken");
			}
			
			client.setUsername(newUsername);
		}
		
		if (
				newPassword != null &&
				!newPassword.isBlank() ) {
			client.setPassword(passwordEncoder.encode(newPassword));
		}
		
		return client;
	}
}
