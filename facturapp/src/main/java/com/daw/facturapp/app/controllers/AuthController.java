package com.daw.facturapp.app.controllers;

import java.security.Principal;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.facturapp.app.models.entities.User;
import com.daw.facturapp.app.models.services.UserServiceImpl;

@Controller
public class AuthController {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserServiceImpl userService;
	

	@GetMapping("/login")
	public String login(@RequestParam(value="error", required=false) String error, 
						@RequestParam(value="logout", required=false) String logout,
						Model model, Principal principal, RedirectAttributes flash,
						Locale locale) {
		// Si ya se había iniciado sesión.
		if(principal != null) {
			flash.addFlashAttribute("info", 
					messageSource.getMessage("text.alert.info.user.already.loggedin", null, locale));
			return "redirect:/";
		}
		
		// Si hay errores en el login.
		if(error != null) {
			model.addAttribute("error", 
					messageSource.getMessage("text.login.alert.error.user.password.wrong", null, locale));
		}
		
		// Si cerramos sesión
		if(logout != null) {
			model.addAttribute("success", 
					messageSource.getMessage("text.login.alert.success.user.logout", null, locale));
		}
		
		model.addAttribute("title", messageSource.getMessage("text.login.title", null, locale));
		
		return "/auth/login";
	}
	
	@GetMapping("/registry")
	public String registry(Model model, Locale locale) {
		model.addAttribute("user", new User());
		model.addAttribute("title", messageSource.getMessage("text.registry.title", null, locale));
		return "/auth/registry";
	}
	
	@PostMapping("/registry")
	public String save(@Valid User user, 
			BindingResult result,
			Model model, 
			Locale locale) {
		// Si hay algún error en la validación de campos.
		if(result.hasErrors()) {
			model.addAttribute("title", messageSource.getMessage("text.registry.title", null, locale));
			model.addAttribute("user", user);
			return "/auth/registry";
		}
		
		try {
			userService.save(user);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			System.out.println(e.getMessage());
			return "/auth/registry";
		}
		
		return "redirect:/";
	}
	
}
