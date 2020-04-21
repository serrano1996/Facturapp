package com.daw.facturapp.app.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daw.facturapp.app.models.dao.IItemInvoiceDao;

@Service
public class ItemInvoiceImpl implements IItemInvoiceService {
	
	@Autowired
	private IItemInvoiceDao itemInvoiceDao;

	@Override
	public void deleteByInvoice(Long id) {
		itemInvoiceDao.deleteByInvoice(id);
	}

}
