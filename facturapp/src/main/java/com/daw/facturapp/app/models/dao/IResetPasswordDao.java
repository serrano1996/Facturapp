package com.daw.facturapp.app.models.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.daw.facturapp.app.models.entities.ResetPassword;

public interface IResetPasswordDao extends CrudRepository<ResetPassword, Long> {
	
	public ResetPassword findByToken(String token);
	
	@Query(value="SELECT * FROM reset_password WHERE user_id=?1", 
			nativeQuery=true)
	public ResetPassword findByUser(Long user_id);
	
	@Modifying
	@Transactional
	@Query(value="DELETE FROM reset_password WHERE user_id=?1", 
			nativeQuery=true)
	public void deleteByUser(Long user_id);

}
