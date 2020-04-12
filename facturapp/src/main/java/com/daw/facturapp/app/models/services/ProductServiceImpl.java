package com.daw.facturapp.app.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.daw.facturapp.app.models.dao.IProductDao;
import com.daw.facturapp.app.models.entities.Product;

@Service
public class ProductServiceImpl implements IProductService {
	
	@Autowired
	private IProductDao productDao;
	
	@Override
	public Product findById(Long id) throws Exception {
		Product product = productDao.findById(id).orElseThrow(() -> 
			new Exception("Product not found"));
		return product;
	}
	
	@Override
	public Page<Product> findByEnterprise(Long enterprise_id, Pageable pageable) {
		return productDao.findByEnterprise(enterprise_id, pageable);
	}

	@Override
	public Product save(Product product) {
		return productDao.save(product);
	}

	@Override
	public List<Product> findByName(String term, Long enterprise_id) {
		return productDao.findByLongNameLikeIgnoreCase(term, enterprise_id);
	}

}
