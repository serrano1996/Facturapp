package com.daw.facturapp.app.models.services;

import com.daw.facturapp.app.models.entities.Invoice;

public interface IInvoiceService {
	
	public Invoice findById(Long id) throws Exception;
	
	public Invoice save(Invoice invoice);
	
	public void delete(Long id);

}
