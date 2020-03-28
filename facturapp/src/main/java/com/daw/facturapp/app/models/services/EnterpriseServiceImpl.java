package com.daw.facturapp.app.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.facturapp.app.models.dao.IEnterpriseDao;
import com.daw.facturapp.app.models.entities.Enterprise;

@Service
public class EnterpriseServiceImpl implements IEnterpriseService {
	
	@Autowired
	private IEnterpriseDao enterpriseDao;

	@Override
	public Enterprise save(Enterprise enterprise) {
		return enterpriseDao.save(enterprise);
	}

	@Override
	public Enterprise findById(Long id) throws Exception {
		Enterprise enterprise = enterpriseDao.findById(id).orElseThrow(() -> 
				new Exception("ff"));
		return enterprise;
	}

}
