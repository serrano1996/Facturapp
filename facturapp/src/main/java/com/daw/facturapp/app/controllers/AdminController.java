package com.daw.facturapp.app.controllers;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.facturapp.app.models.entities.User;
import com.daw.facturapp.app.models.services.UserServiceImpl;
import com.daw.facturapp.app.utils.paginator.PageRender;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping({"", "/", "/index", "/home"})
	public String index(Model model, Locale locale) {
		model.addAttribute("title", messageSource.getMessage("text.admin.home.title", null, locale));
		return "admin/index";
	}
	
	@GetMapping("/users")
	public String users(@RequestParam(name="page", defaultValue="0") int page, 
			Model model, Locale locale) {
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<User> users = userService.findAll(pageRequest);
		PageRender<User> pageRender = new PageRender<User>("/admin/users", users);
		model.addAttribute("title", messageSource.getMessage("text.admin.users.title", null, locale));
		model.addAttribute("users", users);
		model.addAttribute("page", pageRender);
		return "admin/manage_users";
	}

}
