package org.webocr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.webocr.data.service.TemplateService;
import org.webocr.model.Template;

/**
 * Database initialiser. Write all the data source initialisation code here.
 * 
 * @author Saminda Peramuna
 *
 */
@Component
public class WebOcrDataLoader implements ApplicationRunner {

    @Autowired
    private TemplateService templateService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
	templateService.save(new Template("vic_computer", "VIC Computer"));
	templateService.save(new Template("blvd_california", "Boulevard California"));
    }
}
