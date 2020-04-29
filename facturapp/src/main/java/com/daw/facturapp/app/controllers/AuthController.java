package com.daw.facturapp.app.controllers;

import java.security.Principal;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.facturapp.app.models.dao.IUserVerifiedDao;
import com.daw.facturapp.app.models.entities.User;
import com.daw.facturapp.app.models.entities.UserVerified;
import com.daw.facturapp.app.models.services.EmailSenderService;
import com.daw.facturapp.app.models.services.UserServiceImpl;

@Controller
public class AuthController {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private IUserVerifiedDao userVerifiedDao;
	
	@Autowired
	private EmailSenderService emailSenderService;
	
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
					messageSource.getMessage("text.login.alert.error.user.login.wrong", null, locale));
		}
		
		// Si cerramos sesión
		if(logout != null) {
			model.addAttribute("success", 
					messageSource.getMessage("text.login.alert.success.user.logout", null, locale));
		}
		
		model.addAttribute("title", messageSource.getMessage("text.login.title", null, locale));
		
		return "auth/login";
	}
	
	@GetMapping("/registry")
	public String registry(Model model, Locale locale) {
		model.addAttribute("user", new User());
		model.addAttribute("title", messageSource.getMessage("text.registry.title", null, locale));
		return "auth/registry";
	}
	
	@PostMapping("/registry")
	public String save(@Valid User user, 
			BindingResult result,
			Model model, 
			Locale locale,
			RedirectAttributes flash) {
		// Si hay algún error en la validación de campos.
		if(result.hasErrors()) {
			model.addAttribute("title", messageSource.getMessage("text.registry.title", null, locale));
			model.addAttribute("user", user);
			return "/auth/registry";
		}
		
		user.setVerified(false);
		
		try {
			userService.save(user, 0);
			UserVerified verified = new UserVerified(user);
			userVerifiedDao.save(verified);
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(user.getEmail());
			mail.setSubject("Complete your registration!");
			mail.setFrom("facturapp.spring@gmail.com");
			mail.setText("To confirm your account, please click here: " + 
					"http://localhost:8080/confirm-account?token=" + verified.getToken());
			emailSenderService.sendMail(mail);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			System.out.println(e.getMessage());
			return "/auth/registry";
		}
		
		flash.addFlashAttribute("success", messageSource.getMessage("text.login.alert.success.user.record", null, locale));
		
		return "redirect:/";
	}
	
	@GetMapping("/confirm-account")
	public String confirmUserAccount(Model model, 
			@RequestParam("token") String token,
			RedirectAttributes flash) throws Exception {
		UserVerified verified = userVerifiedDao.findByToken(token);
		if(verified != null) {
			User user = userService.findByEmail(verified.getUser().getEmail());
			user.setVerified(true);
			user.setConfirmPassword(user.getPassword());
			userService.save(user, 1);
			flash.addFlashAttribute("success", "usuario verificado");
			return "redirect:/";
		}
		return "redirect:/";
	}
	
}
