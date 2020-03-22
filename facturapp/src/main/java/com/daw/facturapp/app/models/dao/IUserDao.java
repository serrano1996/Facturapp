package com.daw.facturapp.app.models.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.facturapp.app.models.entities.User;

public interface IUserDao extends JpaRepository<User, Long> {
	
	public User findByUsername(String username);
	
	//public Optional<User> findByUsername(String username);
	
	//public Optional<User> findByIdAndPassword(Long id, String password);

}
