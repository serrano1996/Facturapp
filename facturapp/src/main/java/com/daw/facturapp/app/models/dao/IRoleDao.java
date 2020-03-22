package com.daw.facturapp.app.models.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.daw.facturapp.app.models.entities.Role;

@Repository
public interface IRoleDao extends CrudRepository<Role, Long>{
	
	public Role findByName(String name);

}
