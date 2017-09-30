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

    @Column(name = "template_path")
    private String templatePath;

    @Column(name = "image_path")
    private String imagePath;

    protected Template() {

    }

    public Template(String templatePath, String imagePath) {
	this.templatePath = templatePath;
	this.imagePath = imagePath;
    }

    public long getTemplateId() {
	return templateId;
    }

    public String getTemplatePath() {
	return templatePath;
    }

    public String getImagePath() {
	return imagePath;
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

	if (this.templateId == template.templateId && this.templatePath.equals(template.templatePath)
		&& this.imagePath.equals(template.imagePath)) {
	    return true;
	} else {
	    return false;
	}
    }
}