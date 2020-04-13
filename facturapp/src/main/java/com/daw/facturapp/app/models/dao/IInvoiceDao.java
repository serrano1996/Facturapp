package com.daw.facturapp.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.daw.facturapp.app.models.entities.Invoice;

public interface IInvoiceDao extends CrudRepository<Invoice, Long> {

}
