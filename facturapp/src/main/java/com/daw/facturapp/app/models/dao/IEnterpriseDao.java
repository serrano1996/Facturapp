package com.daw.facturapp.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.daw.facturapp.app.models.entities.Enterprise;

public interface IEnterpriseDao extends CrudRepository<Enterprise, Long> {

}
