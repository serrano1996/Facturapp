package com.daw.facturapp.app.models.services;

import com.daw.facturapp.app.models.entities.User;

public interface IUserService {

	public User findByUsername(String username);
	
	public void save(User user) throws Exception;
	
}
