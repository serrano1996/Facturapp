package com.daw.facturapp.app.models.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.daw.facturapp.app.models.entities.ItemInvoice;

public interface IItemInvoiceDao extends CrudRepository<ItemInvoice, Long> {

	@Modifying
	@Transactional
	@Query(value="DELETE FROM item_invoices WHERE invoice_id=?1", 
			nativeQuery=true)
	public void deleteByInvoice(Long invoice_id);
	
}
