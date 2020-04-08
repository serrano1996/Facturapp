package com.daw.facturapp.app.controllers;

import java.io.IOException;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ClientCodecConfigurer.ClientDefaultCodecs;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.facturapp.app.models.entities.Client;
import com.daw.facturapp.app.models.entities.Enterprise;
import com.daw.facturapp.app.models.entities.User;
import com.daw.facturapp.app.models.services.ClientServiceImpl;
import com.daw.facturapp.app.models.services.EnterpriseServiceImpl;
import com.daw.facturapp.app.models.services.UserServiceImpl;
import com.daw.facturapp.app.utils.paginator.PageRender;

@Controller
@RequestMapping("/enterprise")
@SessionAttributes("enterprise")
public class EnterpriseController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private EnterpriseServiceImpl enterpriseService;
	
	@Autowired
	private ClientServiceImpl clientService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	/***********************************************************************/
	//		ENTERPRISE
	/***********************************************************************/
	
	@GetMapping("/{id}")
	public String index(Model model, Locale locale, Authentication auth,
			@PathVariable Long id, RedirectAttributes flash) {
		User user = (User) userService.findByUsername(auth.getName());
		
		Enterprise enterprise = null;
		try {
			enterprise = enterpriseService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.enterprise.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, enterprise)) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
			
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
	public String delete(@PathVariable Long id, Model model, 
		Locale locale, Authentication auth, RedirectAttributes flash) throws Exception {
		User user = (User) userService.findByUsername(auth.getName());
		
		Enterprise enterprise = null;
		try {
			enterprise = enterpriseService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.enterprise.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, enterprise)) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		
		clientService.deletebyEnterprise(enterprise.getId());
		user.removeEnterprise(enterprise.getName());
		enterprise.getClients().clear();
		enterpriseService.delete(id);
		
		flash.addFlashAttribute("success", messageSource.getMessage("text.enterprise.alert.success.delete", null, locale));
		return "redirect:/";
	}

	/***********************************************************************/
	//		COSTUMERS
	/***********************************************************************/
	
	@GetMapping("/{id}/clients")
	public String clients(@PathVariable Long id, Model model,
			Locale locale, Authentication auth,
			@RequestParam(name="page", defaultValue="0") int page,
			RedirectAttributes flash) {
		User user = (User) userService.findByUsername(auth.getName());

		Enterprise enterprise = null;
		try {
			enterprise = enterpriseService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.enterprise.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, enterprise)) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		// Paginación de clientes.
		Pageable pageRequest = PageRequest.of(page, 4);
		Page<Client> clients = 
				clientService.findByEnterprise(enterprise.getId(), pageRequest);
		PageRender<Client> pageRender = 
				new PageRender<Client>("/enterprise/" + enterprise.getId() + "/clients", clients);

		model.addAttribute("enterprise", enterprise);
		model.addAttribute("clients", clients);
		model.addAttribute("page", pageRender);
		model.addAttribute("user", user);
		model.addAttribute("title", enterprise.getName());
		return "enterprise/clients";
	}
	
	@GetMapping("/{id}/add_clients")
	public String addClient(@PathVariable Long id, Model model,
			Locale locale, Authentication auth, RedirectAttributes flash) {
		User user = (User) userService.findByUsername(auth.getName());

		Enterprise enterprise = null;
		try {
			enterprise = enterpriseService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.enterprise.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, enterprise)) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		model.addAttribute("user", user);
		model.addAttribute("title", enterprise.getName());
		model.addAttribute("client", new Client());
		return "enterprise/add_client";
	}
	
	@PostMapping("/add_client")
	public String addClient(@Valid Client client, BindingResult result,
			@RequestParam("enterprise") Long id,
			Model model, Locale locale,
			Authentication auth,
			RedirectAttributes flash) {
		User user = (User) userService.findByUsername(auth.getName());
		
		Enterprise enterprise = null;
		try {
			enterprise = enterpriseService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.enterprise.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, enterprise)) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		if(result.hasErrors()) {
			model.addAttribute("title", enterprise.getName());
			model.addAttribute("enterprise", enterprise);
			return "enterprise/add_client";
		}
		
		enterprise.addClient(client);
		clientService.save(client);
		flash.addFlashAttribute("enterprise", enterprise);
		flash.addFlashAttribute("success", 
				messageSource.getMessage("text.client.alert.success.create", null, locale));
		
		return "redirect:/enterprise/" + enterprise.getId();
	}
	
	@PostMapping("/client/save")
	public String saveClient(@RequestParam("client") Long id,
			@RequestParam("enterprise") Long enterprise,
			@RequestParam("nif") String nif,
			@RequestParam("name") String name,
			RedirectAttributes flash,
			Authentication auth,
			Locale locale)  {
		User user = (User) userService.findByUsername(auth.getName());
		
		Enterprise e;
		try {
			e = enterpriseService.findById(enterprise);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.enterprise.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, e)) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		Client client;
		try {
			client = clientService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.client.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!clientService.isCostumerBelongsToEnterprise(client, e)) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.enterprise.client.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		// Verificación de campos vacios.
		if(nif.equals("") || name.equals("")) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.client.alert.error.edit", null, locale));
			return "redirect:/enterprise/" + enterprise + "/clients";
		}

		client.setNif(nif);
		client.setName(name);
		clientService.save(client);
		flash.addFlashAttribute("success", 
				messageSource.getMessage("text.client.alert.success.edit", null, locale));
		return "redirect:/enterprise/" + enterprise + "/clients";
	}
	
	@PostMapping("/client/delete")
	public String deleteClient(@RequestParam("client") Long id,
			@RequestParam("enterprise") Long enterprise,
			@RequestParam("password") String password,
			RedirectAttributes flash,
			Authentication auth,
			Locale locale) {
		User user = (User) userService.findByUsername(auth.getName());
		
		Enterprise e;
		try {
			e = enterpriseService.findById(enterprise);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.enterprise.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, e)) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		Client client;
		try {
			client = clientService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.client.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!clientService.isCostumerBelongsToEnterprise(client, e)) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.enterprise.client.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		// Verificación del borrado.
		if(!passwordEncoder.matches(password, user.getPassword())) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.no.validation", null, locale));
			return "redirect:/enterprise/" + enterprise + "/clients";
		}

		e.removeClient(client.getId());
		clientService.delete(client.getId());
		
		flash.addFlashAttribute("success", 
				messageSource.getMessage("text.client.alert.success.delete", null, locale));
		return "redirect:/enterprise/" + enterprise + "/clients";
	}
	
	@GetMapping(value = "/load_client/{id}", produces = { "application/json" })
	public @ResponseBody Client loadClient(@PathVariable Long id) throws Exception {
		return clientService.findById(id);
	}
	
	/***********************************************************************/
	//		PRODUCTS
	/***********************************************************************/
	
	//@GetMapping("/{id}/products")
	//public String products(@PathVariable Long id, Model model,
	//		Locale locale, Authentication auth) throws Exception {
	//	User user = (User) userService.findByUsername(auth.getName());
	//	Enterprise enterprise = enterpriseService.findById(id);
	//	model.addAttribute("user", user);
	//	model.addAttribute("title", enterprise.getName());
	//	return "enterprise/products";
	//}
	
}
