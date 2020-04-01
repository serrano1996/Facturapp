package com.daw.facturapp.app.controllers;

import java.io.IOException;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.daw.facturapp.app.models.entities.Enterprise;
import com.daw.facturapp.app.models.entities.User;
import com.daw.facturapp.app.models.services.EnterpriseServiceImpl;
import com.daw.facturapp.app.models.services.UserServiceImpl;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private EnterpriseServiceImpl enterpriseService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping("/add_business")
	public String addBusiness(Model model, Locale locale, Authentication auth) {
		User user = (User) userService.findByUsername(auth.getName());
		model.addAttribute("user", user);
		model.addAttribute("enterprise", new Enterprise());
		model.addAttribute("title", messageSource.getMessage("text.user.add.enterprise.form.title", null, locale));
		return "user/add_business";
	}
	
	@PostMapping("/add_business")
	public String addBusiness(Model model, Locale locale,
			@Valid Enterprise enterprise, BindingResult result,
			@RequestParam("image") MultipartFile image,
			Authentication auth) throws Exception {
		User user = (User) userService.findByUsername(auth.getName());
		
		if(result.hasErrors()) {
			model.addAttribute("title", messageSource.getMessage("text.user.add.enterprise.form.title", null, locale));
			model.addAttribute("enterprise", enterprise);
			return "user/add_business";
		}
		
		if (!image.isEmpty()) {			
			try {
				byte[] img = image.getBytes();
				enterprise.setLogo(img);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		enterpriseService.save(enterprise);
		user.getEnterprises().add(enterprise);
		userService.save(user, 1);
		model.addAttribute("user", user);
		
		return "index";
	}

}
