package com.daw.facturapp.app.models.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.daw.facturapp.app.models.entities.UserVerified;

public interface IUserVerifiedDao extends CrudRepository<UserVerified, Long> {

	public UserVerified findByToken(String token);
	
	@Modifying
	@Transactional
	@Query(value="DELETE FROM users_verified WHERE user_id=?1", 
			nativeQuery=true)
	public void deleteByUser(Long user_id);
	
}
