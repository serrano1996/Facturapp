package com.daw.facturapp.app.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.facturapp.app.models.dao.IEnterpriseDao;
import com.daw.facturapp.app.models.entities.Enterprise;
import com.daw.facturapp.app.models.entities.User;

@Service
public class EnterpriseServiceImpl implements IEnterpriseService {
	
	@Autowired
	private IEnterpriseDao enterpriseDao;
	
	@Override
	public Enterprise findById(Long id) throws Exception {
		Enterprise enterprise = enterpriseDao.findById(id).orElseThrow(() -> 
				new Exception("Enterprise not found"));
		return enterprise;
	}

	@Override
	public Enterprise save(Enterprise enterprise) {
		return enterpriseDao.save(enterprise);
	}

	@Override
	public void delete(Long id) {
		enterpriseDao.deleteById(id);	
	}
	
	public boolean isEnterpriseBelongsToUser(User user, Enterprise enterprise) {
		for(Enterprise e : user.getEnterprises()) {
			if(e.getId().equals(enterprise.getId())) {
				return true;
			}
		}
		return false;
	}

}
