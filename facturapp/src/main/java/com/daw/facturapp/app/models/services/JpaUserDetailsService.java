package com.daw.facturapp.app.models.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daw.facturapp.app.models.dao.IUserDao;
import com.daw.facturapp.app.models.entities.Role;
import com.daw.facturapp.app.models.entities.User;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {
	
	@Autowired
	private IUserDao userDao;
	
	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);

	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//User user = userDao.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Login invalido"));
		
		//User user = userDao.findByUsername(username);
		
		
		/*if(user == null) {
			logger.error("Error login: el usuario '" + username + "' no existe");
			throw new UsernameNotFoundException("El usuario '" + username + "' no existe en el sistema");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for(Role role : user.getRoles()) {
			logger.info("Role: " + role.getAuthority());
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		
		if(authorities.isEmpty()) {
			logger.error("Error login: el usuario '" + username + "' no tiene roles asignados");
			throw new UsernameNotFoundException("El usuario '" + username + "' no tiene roles en el sistema");
		}
		
		return new org.springframework.security.core.userdetails.User(
					user.getUsername(), 
					user.getPassword(), 
					user.getEnabled(), 
					true, 
					true, 
					true, 
					authorities
				);*/
		return null;
	}	

}
