package com.daw.facturapp.app.controllers;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String index(Model model, Locale locale, Authentication auth) {
		User user = (User) userService.findByUsername(auth.getName());
		model.addAttribute("user", user);
		model.addAttribute("title", messageSource.getMessage("text.admin.home.title", null, locale));
		return "admin/index";
	}
	
	@GetMapping("/users")
	public String users(@RequestParam(name="page", defaultValue="0") int page, 
			Model model, Locale locale, Authentication auth) {
		User user = (User) userService.findByUsername(auth.getName());
		model.addAttribute("user", user);
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<User> users = userService.findAll(pageRequest);
		PageRender<User> pageRender = new PageRender<User>("/admin/users", users);
		model.addAttribute("title", messageSource.getMessage("text.admin.users.title", null, locale));
		model.addAttribute("users", users);
		model.addAttribute("page", pageRender);
		return "admin/manage_users";
	}
	
	@PostMapping("/lock/{id}")
	public String lockUser(@PathVariable(value="id") Long id, Model model,
			@RequestParam(name="page", defaultValue="0") int page,
			RedirectAttributes flash, Locale locale, Authentication auth) throws Exception {
		User u = (User) userService.findByUsername(auth.getName());
		model.addAttribute("user", u);
		User user = userService.findById(id);
		user.setEnabled(false);
		user.setConfirmPassword("0");
		userService.save(user, 1);
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<User> users = userService.findAll(pageRequest);
		PageRender<User> pageRender = new PageRender<User>("/admin/users", users);
		model.addAttribute("title", messageSource.getMessage("text.admin.users.title", null, locale));
		model.addAttribute("users", users);
		model.addAttribute("page", pageRender);
		return "redirect:/admin/users";
	}
	
	@PostMapping("/unlock/{id}")
	public String unlockUser(@PathVariable(value="id") Long id, Model model,
			@RequestParam(name="page", defaultValue="0") int page,
			RedirectAttributes flash, Locale locale, Authentication auth) throws Exception {
		User u = (User) userService.findByUsername(auth.getName());
		model.addAttribute("user", u);
		User user = userService.findById(id);
		user.setEnabled(true);
		user.setConfirmPassword("0");
		userService.save(user, 1);
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<User> users = userService.findAll(pageRequest);
		PageRender<User> pageRender = new PageRender<User>("/admin/users", users);
		model.addAttribute("title", messageSource.getMessage("text.admin.users.title", null, locale));
		model.addAttribute("users", users);
		model.addAttribute("page", pageRender);
		return "redirect:/admin/users";
	}
	
	@PostMapping("/search")
	public String search(Model model, @RequestParam("username") String username,
			@RequestParam(name="page", defaultValue="0") int page, 
			Locale locale, Authentication auth) {
		User user = (User) userService.findByUsername(auth.getName());
		model.addAttribute("user", user);
		List<User> search = userService.findByUsernameLike(username);
		model.addAttribute("search", search);
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<User> users = userService.findAll(pageRequest);
		PageRender<User> pageRender = new PageRender<User>("/admin/users", users);
		model.addAttribute("title", messageSource.getMessage("text.admin.users.title", null, locale));
		model.addAttribute("users", users);
		model.addAttribute("page", pageRender);
		return "admin/manage_users";	
	}
	
	@GetMapping(value = "/load_user/{id}", produces = { "application/json" })
	public @ResponseBody User loadUser(@PathVariable Long id) {
		return userService.findById(id);
	}

}
