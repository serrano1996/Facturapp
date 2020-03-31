package com.daw.facturapp.app.controllers;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.daw.facturapp.app.models.entities.Enterprise;
import com.daw.facturapp.app.models.entities.User;
import com.daw.facturapp.app.models.services.EnterpriseServiceImpl;
import com.daw.facturapp.app.models.services.UserServiceImpl;

@Controller
@RequestMapping("/enterprise")
public class EnterpriseController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private EnterpriseServiceImpl enterpriseService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/{id}")
	public String index(Model model, Locale locale, Authentication auth,
			@PathVariable Long id) throws Exception {
		User user = (User) userService.findByUsername(auth.getName());
		Enterprise enterprise = enterpriseService.findById(id);
		
		model.addAttribute("enterprise", enterprise);
		model.addAttribute("user", user);
		model.addAttribute("title", enterprise.getName());
		return "enterprise/index";
	}

}
