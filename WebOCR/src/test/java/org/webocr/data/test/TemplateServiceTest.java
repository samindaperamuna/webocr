package org.webocr.data.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.webocr.WebOcrApplication;
import org.webocr.data.service.TemplateService;
import org.webocr.model.Template;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebOcrApplication.class)
public class TemplateServiceTest {

    @Autowired
    TemplateService templateService;

    @Test
    public void testSave() {
	Template template = new Template("test.xml", "test.jpg");

	long id = templateService.save(template);

	Template found = templateService.findById(id);

	assertNotNull(found);
	assertEquals(template, found);
    }
}
