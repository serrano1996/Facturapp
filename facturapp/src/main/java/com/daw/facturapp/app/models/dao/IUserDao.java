package com.daw.facturapp.app.models.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.daw.facturapp.app.models.entities.User;

public interface IUserDao extends CrudRepository<User, Long> {
	
	public Optional<User> findByUsername(String username);
	
	public Optional<User> findByEmail(String email);

}
