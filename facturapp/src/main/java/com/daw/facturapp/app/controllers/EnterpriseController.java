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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.facturapp.app.models.entities.Enterprise;
import com.daw.facturapp.app.models.entities.User;
import com.daw.facturapp.app.models.services.EnterpriseServiceImpl;
import com.daw.facturapp.app.models.services.UserServiceImpl;

@Controller
@RequestMapping("/enterprise")
@SessionAttributes("enterprise")
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
	
	@PostMapping("/save")
	public String save(@Valid Enterprise enterprise, Model model,
			Authentication auth, BindingResult result,
			@RequestParam("image") MultipartFile image,
			SessionStatus status,
			RedirectAttributes flash,
			Locale locale) {
		User user = (User) userService.findByUsername(auth.getName());
		
		if(result.hasErrors()) {
			model.addAttribute("title", enterprise.getName());
			model.addAttribute("enterprise", enterprise);
			flash.addFlashAttribute("error", messageSource.getMessage("text.enterprise.alert.error.edit", null, locale));
			return "redirect:/enterprise/" + enterprise.getId();
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
		model.addAttribute("enterprise", enterprise);
		status.setComplete();
		model.addAttribute("user", user);
		flash.addFlashAttribute("success", messageSource.getMessage("text.enterprise.alert.success.edit", null, locale));
		model.addAttribute("title", enterprise.getName());
		return "redirect:/enterprise/" + enterprise.getId();
	}
	
	@PostMapping("/delete/{id}")
	public String delete() {
		return "";
	}

}
