package com.daw.facturapp.app.models.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.daw.facturapp.app.models.entities.Client;

public interface IClientService {
	
	public Client save (Client client);
	
	public Page<Client> findByEnterprise(Long enterprise_id, Pageable pageable);

}
