package com.daw.facturapp.app.models.services;

import com.daw.facturapp.app.models.entities.Enterprise;

public interface IEnterpriseService {
	
	public Enterprise findById(Long id) throws Exception;
	
	public Enterprise save(Enterprise enterprise);

}
