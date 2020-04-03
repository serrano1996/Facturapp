package com.daw.facturapp.app.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.daw.facturapp.app.models.dao.IClientDao;
import com.daw.facturapp.app.models.entities.Client;

@Service
public class ClientServiceImpl implements IClientService {
	
	@Autowired
	private IClientDao clientDao;

	public Client save(Client client) {
		return clientDao.save(client);
	}

	@Override
	public Page<Client> findByEnterprise(Long enterprise_id, Pageable pageable) {
		return clientDao.findByEnterprise(enterprise_id, pageable);
	}

}
