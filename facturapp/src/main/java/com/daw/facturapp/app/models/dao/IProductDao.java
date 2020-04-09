package com.daw.facturapp.app.models.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.daw.facturapp.app.models.entities.Product;

public interface IProductDao extends CrudRepository<Product, Long> {

	@Query(value="SELECT * FROM products WHERE enterprise_id=?1", 
			nativeQuery=true)
	Page<Product> findByEnterprise(Long enterprise_id, Pageable pageable);
	
}
