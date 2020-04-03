package com.daw.facturapp.app.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.daw.facturapp.app.models.entities.Client;

public interface IClientDao extends CrudRepository<Client, Long> {

	@Query(value="SELECT * FROM clients WHERE enterprise_id=?1", 
			nativeQuery=true)
	Page<Client> findByEnterprise(Long enterprise_id, Pageable pageable);
	
}
