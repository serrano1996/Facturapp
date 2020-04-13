package com.daw.facturapp.app.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.facturapp.app.models.dao.IInvoiceDao;
import com.daw.facturapp.app.models.entities.Invoice;

@Service
public class InvoiceServiceImpl implements IInvoiceService {
	
	@Autowired
	private IInvoiceDao invoiceDao;

	@Override
	public Invoice save(Invoice invoice) {
		return invoiceDao.save(invoice);
	}

}