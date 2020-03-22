package com.daw.facturapp.app.models.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.facturapp.app.models.dao.IUserDao;
import com.daw.facturapp.app.models.entities.User;

@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private IUserDao userDao;
	
	//private boolean checkUsernameAvaliable(User user) throws Exception {
		//Optional<User> userFound = this.findByUsername(user.getUsername());
		//if(userFound.isPresent()) {
		//	throw new Exception("Username no disponible");
		//}
		//return true;
	//}

	@Override
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public void save(User user) throws Exception {
		//if(checkUsernameAvaliable(user)) {
			userDao.save(user);
		//}	
	}

}
