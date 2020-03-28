package com.daw.facturapp.app.controllers;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.daw.facturapp.app.models.entities.Enterprise;
import com.daw.facturapp.app.models.entities.User;
import com.daw.facturapp.app.models.services.EnterpriseServiceImpl;
import com.daw.facturapp.app.models.services.UserServiceImpl;

@Controller
public class IndexController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private EnterpriseServiceImpl enterpriseService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping({"", "/", "/index", "/home"})
	public String index(Model model, Locale locale,
			Authentication auth) {
		if(auth != null) {
			User user = (User) userService.findByUsername(auth.getName());
			model.addAttribute("user", user);
		}
		model.addAttribute("title", messageSource.getMessage("text.home.title", null, locale));
		return "index";
	}
	
	@GetMapping("/img/view/{id}")
	public void view(@PathVariable Long id, HttpServletResponse response) throws ServletException, 
		IOException {
		Enterprise enterprise = null;
		
		try {
			enterprise = enterpriseService.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(enterprise.getLogo() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(enterprise.getLogo());
			response.getOutputStream().close();
		} else {
			response.getOutputStream().close();
		}
	}

}
