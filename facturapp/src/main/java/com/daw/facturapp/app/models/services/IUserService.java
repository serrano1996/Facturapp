package com.daw.facturapp.app.models.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.daw.facturapp.app.models.entities.User;

public interface IUserService {
	
	public User findById(Long id);
	
	public List<User> findAll();
	
	public Page<User> findAll(Pageable pageable);
	
	public User save(User user, int mode) throws Exception;
	
}
