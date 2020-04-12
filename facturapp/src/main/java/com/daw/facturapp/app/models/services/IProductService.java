package com.daw.facturapp.app.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.daw.facturapp.app.models.entities.Product;

public interface IProductService {
	
	public Product findById(Long id) throws Exception;
	
	public List<Product> findByName(String term, Long enterprise_id);
	
	public Page<Product> findByEnterprise(Long enterprise_id, Pageable pageable);
	
	public Product save(Product product);

}
