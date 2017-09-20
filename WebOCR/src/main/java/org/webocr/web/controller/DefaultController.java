package org.webocr.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.webocr.model.Invoice;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String home() {
	return "index";
    }

    @GetMapping("/invoice")
    public String getInvoice() {
	return "invoice";
    }

    @PostMapping("/invoice")
    public ModelAndView postInvoice(Invoice invoice) {
	ModelAndView mv = new ModelAndView("invoice");
	mv.addObject("invoice", invoice);

	return mv;
    }
}
