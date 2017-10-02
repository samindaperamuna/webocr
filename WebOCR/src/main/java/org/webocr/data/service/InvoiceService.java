package org.webocr.data.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.webocr.model.Invoice;

@Repository
@Transactional
public class InvoiceService {

    @Autowired
    private EntityManager entityManager;

    public long Save(Invoice invoice) {
	entityManager.persist(invoice);

	return invoice.getInvoiceId();
    }
}
