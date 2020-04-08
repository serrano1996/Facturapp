package com.daw.facturapp.app.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.daw.facturapp.app.models.dao.IClientDao;
import com.daw.facturapp.app.models.entities.Client;
import com.daw.facturapp.app.models.entities.Enterprise;

@Service
public class ClientServiceImpl implements IClientService {
	
	@Autowired
	private IClientDao clientDao;
	
	@Override
	public Client findById(Long id) throws Exception {
		Client client = clientDao.findById(id).orElseThrow(() -> 
			new Exception("Client not found"));
		return client;
	}
	
	@Override
	public Page<Client> findByEnterprise(Long enterprise_id, Pageable pageable) {
		return clientDao.findByEnterprise(enterprise_id, pageable);
	}

	public Client save(Client client) {
		return clientDao.save(client);
	}
	
	@Override
	public void delete(Long id) {
		clientDao.deleteById(id);
		
	}
	
	@Override
	public void deletebyEnterprise(Long id) {
		clientDao.deleteByEnterprise(id);		
	}
	
	public boolean isCostumerBelongsToEnterprise(Client client, Enterprise enterprise) {
		for(Client c : enterprise.getClients()) {
			if(c.getId().equals(client.getId())) {
				return true;
			}
		}
		return false;
	}

}
