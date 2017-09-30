package org.webocr.web.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.webocr.data.service.TemplateService;
import org.webocr.model.Invoice;
import org.webocr.model.Template;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DefaultController {

    @Autowired
    private TemplateService templateService;

    @GetMapping("/")
    public String home() {
	Template template = new Template("template.xml", "image.png");
	templateService.save(template);

	return "index";
    }

    @GetMapping("/invoice")
    public String getInvoice() {
	return "invoice";
    }

    @PostMapping("/invoice")
    public ModelAndView postInvoice(@RequestParam("jsonInvoice") String jsonInvoice) {
	ModelAndView mv = new ModelAndView("invoice");
	ObjectMapper mapper = new ObjectMapper();
	Invoice invoice = null;

	try {
	    invoice = mapper.readValue(jsonInvoice, Invoice.class);
	} catch (IOException e) {
	    System.err.println("Cannot map JSON invoice into an Invoice object.\n" + e.getLocalizedMessage());
	}

	if (invoice != null) {
	    mv.addObject("invoice", invoice);
	}

	return mv;
    }
}
