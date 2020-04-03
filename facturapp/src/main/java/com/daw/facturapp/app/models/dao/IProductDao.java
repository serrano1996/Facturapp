package com.daw.facturapp.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.daw.facturapp.app.models.entities.Product;

public interface IProductDao extends CrudRepository<Product, Long> {

}
