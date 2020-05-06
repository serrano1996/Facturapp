package com.daw.facturapp.app.controllers;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.facturapp.app.models.dao.IResetPasswordDao;
import com.daw.facturapp.app.models.entities.ResetPassword;
import com.daw.facturapp.app.models.entities.User;
import com.daw.facturapp.app.models.services.EmailSenderService;
import com.daw.facturapp.app.models.services.UserServiceImpl;

@Controller
@RequestMapping("/reset")
public class ResetPasswordController {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private IResetPasswordDao resetPasswordDao;
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	@GetMapping("/email") 
	public String confirmEmailReset(Model model, Locale locale) {
		model.addAttribute("title", messageSource.getMessage("text.reset.password.title", null, locale));
		return "auth/email";
	}
	
	@PostMapping("/email-form") 
	public String sendEmailReset(@RequestParam("email") String email,
			RedirectAttributes flash, Locale locale) {
		User user = userService.findByEmail(email);
		
		if(user == null) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.error.email", null, locale));
			return "redirect:/reset/email";
		}
		
		ResetPassword reset = new ResetPassword(user);
		resetPasswordDao.save(reset);
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setSubject("Reset your password!");
		mail.setFrom("facturapp.spring@gmail.com");
		mail.setText("To reset your password, please click here: " + 
				"http://localhost:8080/reset/reset-password?token=" + reset.getToken());
		emailSenderService.sendMail(mail);
		
		flash.addFlashAttribute("info", 
				messageSource.getMessage("text.user.alert.reset.mail", null, locale));
		return "redirect:/";
	}

	
	@GetMapping("/reset-password")
	public String displayResetPassword(@RequestParam("token") String token,
			RedirectAttributes flash, Locale locale, Model model) throws Exception {
		ResetPassword reset = resetPasswordDao.findByToken(token);
		model.addAttribute("title", messageSource.getMessage("text.reset.password.title", null, locale));
		model.addAttribute("token", token);
		model.addAttribute("reseted", reset.getUser());
		return "auth/reset-password";
	}
	
	@PostMapping("reset-password")
	public String resetPassword(@RequestParam("token") String token,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("confirm") String confirm,
			RedirectAttributes flash, Locale locale) throws Exception {
		ResetPassword reset = resetPasswordDao.findByToken(token);
		User u = userService.findByEmail(email);
		
		if(reset == null) {			
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.reset.password", null, locale));
			return "redirect:/";
		}
		
		User user = userService.findByEmail(reset.getUser().getEmail());
		
		if(!u.getEmail().equals(user.getEmail())) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.reset.password", null, locale));
			return "redirect:/";
		}
		
		if(!password.equals(confirm)) {			
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.reset.password.not.match", null, locale));
			return "redirect:/";
		}
			
		user.setPassword(passwordEncoder.encode(password));
		user.setConfirmPassword(passwordEncoder.encode(confirm));
		userService.save(user, 1);
		flash.addFlashAttribute("success", 
				messageSource.getMessage("text.user.reset.password.success", null, locale));
		return "redirect:/";
	}
	
}
