package org.webocr.data.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.webocr.model.Template;

@Repository
@Transactional
public class TemplateService {

    @Autowired
    private EntityManager entityManager;

    public long save(Template template) {
	entityManager.persist(template);

	return template.getTemplateId();
    }

    public Template findById(long id) {
	return entityManager.find(Template.class, id);
    }
}