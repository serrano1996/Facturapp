package com.daw.facturapp.app.controllers;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@GetMapping("/{id}/profile")
	public String user(@PathVariable Long id, Model model,
			Locale locale, Authentication auth,
			RedirectAttributes flash) {
		User user = userService.findById(id);
		
		if(user == null) {
			flash.addFlashAttribute("error", 
				messageSource.getMessage("text.enterprise.error.not.found", null, locale));
			return "redirect:/";
		}
		
		if(!user.getUsername().equals(auth.getName())) {
			flash.addFlashAttribute("error", 
				messageSource.getMessage("text.user.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		model.addAttribute("title", user.getUsername());
		model.addAttribute("user", user);
		return "user/profile";
	}
	
	@PostMapping("/save")
	public String save(Model model, Authentication auth,
			@RequestParam("name") String name,
			@RequestParam("lastname") String lastname,
			@RequestParam("email") String email,
			@RequestParam("photo") MultipartFile photo,
			SessionStatus status,
			RedirectAttributes flash,
			Locale locale) throws Exception {
		User user = userService.findByUsername(auth.getName());
		
		if (!photo.isEmpty()) {			
			try {
				byte[] img = photo.getBytes();
				user.setImage(img);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		user.setName(name);
		user.setLastname(lastname);
		user.setEmail(email);
		user.setConfirmPassword(user.getPassword());
		userService.save(user, 1);
		status.setComplete();
		model.addAttribute("user", user);
		flash.addFlashAttribute("success", messageSource.getMessage("text.user.alert.success.edit", null, locale));
		model.addAttribute("title", user.getUsername());
		return "redirect:/user/" + user.getId() + "/profile";
	}
	
	@GetMapping("/add_business")
	public String addBusiness(Model model, Locale locale, Authentication auth) {
		User user = (User) userService.findByUsername(auth.getName());
		model.addAttribute("user", user);
		model.addAttribute("enterprise", new Enterprise());
		model.addAttribute("title", messageSource.getMessage("text.user.add.enterprise.form.title", null, locale));
		return "user/add_business";
	}
	
	@PostMapping("/add_business")
	public String addBusiness(@Valid Enterprise enterprise, BindingResult result, 
			@RequestParam("image") MultipartFile image,
			Model model, Locale locale,
			Authentication auth,
			RedirectAttributes flash) throws Exception {
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
		
		flash.addFlashAttribute("success", messageSource.getMessage("text.enterprise.alert.success.create", null, locale));
		enterpriseService.save(enterprise);
		user.getEnterprises().add(enterprise);
		userService.save(user, 1);
		model.addAttribute("user", user);
		
		return "redirect:/";
	}

	@GetMapping("/img/{id}")
	public void view(@PathVariable Long id, HttpServletResponse response) throws ServletException, 
		IOException {
		User user = null;
		
		try {
			user = userService.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(user.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(user.getImage());
			response.getOutputStream().close();
		} else {
			response.getOutputStream().close();
		}
	}
	
}
