package com.daw.facturapp.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.daw.facturapp.app.models.entities.UserVerified;

public interface IUserVerifiedDao extends CrudRepository<UserVerified, Long> {

	public UserVerified findByToken(String token);
	
}
