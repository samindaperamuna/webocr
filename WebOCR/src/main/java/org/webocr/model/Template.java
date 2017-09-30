package org.webocr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "template")
public class Template {

    @Id
    @Column(name = "template_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long templateId;

    @Column(name = "template_name", unique = true)
    private String templateName;

    @Column(name = "template_desc")
    private String templateDesc;

    protected Template() {

    }

    public Template(String templateName) {
	this.templateName = templateName;
    }

    public Template(String templateName, String templateDesc) {
	this.templateName = templateName;
	this.templateDesc = templateDesc;
    }

    public long getTemplateId() {
	return templateId;
    }

    public String getTemplateName() {
	return templateName;
    }

    public String getTemplateDesc() {
	return templateDesc;
    }

    public void setTemplateDesc(String templateDesc) {
	this.templateDesc = templateDesc;
    }

    @Override
    public boolean equals(Object object) {
	if (object == null) {
	    return false;
	}

	if (!(object instanceof Template)) {
	    return false;
	}

	Template template = (Template) object;

	if (this.templateId == template.templateId && this.templateName.equals(template.templateName)) {
	    return true;
	} else {
	    return false;
	}
    }
}