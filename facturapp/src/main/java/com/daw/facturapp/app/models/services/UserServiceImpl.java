package com.daw.facturapp.app.models.services;

import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import com.daw.facturapp.app.models.dao.IRoleDao;
import com.daw.facturapp.app.models.dao.IUserDao;
import com.daw.facturapp.app.models.entities.Role;
import com.daw.facturapp.app.models.entities.User;

@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IRoleDao roleDao;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private LocaleResolver localeResolver;
	
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * Valida que el nombre de usuario sea único.
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private boolean checkUsernameAvaliable(User user) throws Exception {
		Locale locale = localeResolver.resolveLocale(request);
		Optional<User> userFound = userDao.findByUsername(user.getUsername());
		if(userFound.isPresent()) {
			throw new Exception(messageSource.getMessage("text.registry.alert.error.username.not.avaliable", null, locale));
		}
		return true;
	}
	
	/**
	 * Valida que el correo sea único.
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private boolean checkEmailAvaliable(User user) throws Exception {
		Locale locale = localeResolver.resolveLocale(request);
		Optional<User> userFound = userDao.findByEmail(user.getEmail());
		if(userFound.isPresent()) {
			throw new Exception(messageSource.getMessage("text.registry.alert.error.email.not.avaliable", null, locale));
		}
		return true;
	}
	
	/**
	 * Comprueba la confirmación de la contraseña.
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private boolean checkPasswordMatch(User user) throws Exception {
		Locale locale = localeResolver.resolveLocale(request);
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			throw new Exception(messageSource.getMessage("text.registry.alert.error.password.not.match", null, locale));
		}
		return true;
	}

	@Override
	public User save(User user) throws Exception {
		if(checkUsernameAvaliable(user) && checkPasswordMatch(user) && 
				checkEmailAvaliable(user)) {
			Role role = roleDao.findByName("USER");
			user.getRoles().add(role);
			user = userDao.save(user);
		}
		return user;
	}

}
