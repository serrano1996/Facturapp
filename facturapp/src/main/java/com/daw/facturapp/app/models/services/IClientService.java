package com.daw.facturapp.app.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.daw.facturapp.app.models.entities.Client;

public interface IClientService {
	
	public Client findById(Long id) throws Exception;
	
	public Client save(Client client);
	
	public Page<Client> findByEnterprise(Long enterprise_id, Pageable pageable);
	
	public List<Client> findByNameAndEnterprise(String name, Long enterprise_id);
	
	public void delete(Long id);
	
	public void deletebyEnterprise(Long id);

}
