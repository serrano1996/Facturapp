package com.daw.facturapp.app.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.daw.facturapp.app.models.entities.User;

public interface IUserDao extends PagingAndSortingRepository<User, Long> {
	
	@Query("select u from User u where u.username like %?1%")
	List<User> findByUsernameLike(String username);
	
	public Optional<User> findByUsername(String username);
	
	public Optional<User> findByEmail(String email);

}
