package com.daw.facturapp.app.models.services;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.daw.facturapp.app.models.dao.IUserDao;
import com.daw.facturapp.app.models.entities.Role;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private IUserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Busqueda del usuario.
		com.daw.facturapp.app.models.entities.User appUser = userDao.findByUsername(username).orElseThrow(() -> 
			new UsernameNotFoundException("Usuario desconocido"));
		// Asignación de roles de usuario.
		Set<GrantedAuthority> grants = new HashSet<GrantedAuthority>();
		for(Role role: appUser.getRoles()) {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getDescription());
			grants.add(grantedAuthority);
		}
		// Usuario que se cargará en la sesión.
		UserDetails user = (UserDetails) new User(username, appUser.getPassword(), grants);
		return user;
	}

}
