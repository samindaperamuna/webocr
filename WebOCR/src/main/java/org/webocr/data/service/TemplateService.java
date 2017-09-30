package org.webocr.data.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.webocr.model.Template;

@Repository
@Transactional
public class TemplateService {

    private static final String FIND_ALL = "select t from Template t";

    @Autowired
    private EntityManager entityManager;

    public long save(Template template) {
	entityManager.persist(template);

	return template.getTemplateId();
    }

    public Template findById(long id) {
	return entityManager.find(Template.class, id);
    }

    public Template findByName(String name) {
	return entityManager.find(Template.class, name);
    }

    public List<Template> findAll() {
	TypedQuery<Template> query = entityManager.createQuery(FIND_ALL, Template.class);

	return query.getResultList();
    }
}