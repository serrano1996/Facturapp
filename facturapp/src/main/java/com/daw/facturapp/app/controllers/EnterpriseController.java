package com.daw.facturapp.app.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.daw.facturapp.app.models.entities.Invoice;
import com.daw.facturapp.app.models.entities.ItemInvoice;
import com.daw.facturapp.app.models.entities.Product;
import com.daw.facturapp.app.models.entities.User;
import com.daw.facturapp.app.models.services.ClientServiceImpl;
import com.daw.facturapp.app.models.services.EnterpriseServiceImpl;
import com.daw.facturapp.app.models.services.InvoiceServiceImpl;
import com.daw.facturapp.app.models.services.ItemInvoiceImpl;
import com.daw.facturapp.app.models.services.ProductServiceImpl;
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
	private ProductServiceImpl productService;
	
	@Autowired
	private InvoiceServiceImpl invoiceService;
	
	@Autowired
	private ItemInvoiceImpl itemInvoiceService;
	
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
		return "enterprise/client/clients";
	}
	
	@PostMapping("client/search")
	public String searchClient(@RequestParam("client") String name, 
			@RequestParam("enterpriseId") Long enterpriseId,
			Locale locale, Authentication auth, 
			RedirectAttributes flash) {
		User user = (User) userService.findByUsername(auth.getName());

		Enterprise enterprise = null;
		try {
			enterprise = enterpriseService.findById(enterpriseId);
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
		
		flash.addFlashAttribute("search", 
				clientService.findByNameAndEnterprise(name, enterprise.getId()));
		
		return "redirect:/enterprise/" + enterprise.getId() + "/clients";
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
		return "enterprise/client/add_client";
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
			return "enterprise/client/add_client";
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
			@RequestParam("nif") String nif,
			@RequestParam("name") String name,
			RedirectAttributes flash,
			Authentication auth,
			Locale locale)  {
		User user = (User) userService.findByUsername(auth.getName());
		
		Client client;
		try {
			client = clientService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.client.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		Enterprise enterprise = client.getEnterprise();
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, enterprise)) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		if(!clientService.isCostumerBelongsToEnterprise(client, enterprise)) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.enterprise.client.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		// Verificación de campos vacios.
		if(nif.equals("") || name.equals("")) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.client.alert.error.edit", null, locale));
			return "redirect:/enterprise/" + client.getEnterprise().getId() + "/clients";
		}

		client.setNif(nif);
		client.setName(name);
		clientService.save(client);
		flash.addFlashAttribute("success", 
				messageSource.getMessage("text.client.alert.success.edit", null, locale));
		return "redirect:/enterprise/" + client.getEnterprise().getId() + "/clients";
	}
	
	@GetMapping("/client/{id}")
	public String showClient(@PathVariable Long id, Model model,
			Locale locale, Authentication auth,
			RedirectAttributes flash) {
		User user = (User) userService.findByUsername(auth.getName());
		
		Client client;
		try {
			client = clientService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.client.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, client.getEnterprise())) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		model.addAttribute("user", user);
		model.addAttribute("client", client);
		model.addAttribute("title", client.getEnterprise().getName());
		return "enterprise/client/show_client";
	}
	
	@PostMapping("/client/delete")
	public String deleteClient(@RequestParam("client") Long id,
			@RequestParam("password") String password,
			RedirectAttributes flash,
			Authentication auth,
			Locale locale) {
		User user = (User) userService.findByUsername(auth.getName());
		
		Client client;
		try {
			client = clientService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.client.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, client.getEnterprise())) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		if(!clientService.isCostumerBelongsToEnterprise(client, client.getEnterprise())) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.enterprise.client.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		// Verificación del borrado.
		if(!passwordEncoder.matches(password, user.getPassword())) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.no.validation", null, locale));
			return "redirect:/enterprise/" + client.getEnterprise().getId() + "/clients";
		}

		client.getEnterprise().removeClient(client.getId());
		clientService.delete(client.getId());
		
		flash.addFlashAttribute("success", 
				messageSource.getMessage("text.client.alert.success.delete", null, locale));
		return "redirect:/";
	}
	
	@GetMapping(value = "/load_client/{id}", produces = { "application/json" })
	public @ResponseBody Client loadClient(@PathVariable Long id) throws Exception {
		Client client = clientService.findById(id);
		client.setEnterprise(null);
		client.setInvoices(null);
		return client;
	}
	
	/***********************************************************************/
	//		PRODUCTS
	/***********************************************************************/
	
	@GetMapping("/{id}/products")
	public String products(@PathVariable Long id, Model model,
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
		
		// Paginación de productos.
		Pageable pageRequest = PageRequest.of(page, 6);
		Page<Product> products = 
				productService.findByEnterprise(enterprise.getId(), pageRequest);
		PageRender<Product> pageRender = 
				new PageRender<Product>("/enterprise/" + enterprise.getId() + "/products", products);

		model.addAttribute("enterprise", enterprise);
		model.addAttribute("products", products);
		model.addAttribute("page", pageRender);
		model.addAttribute("user", user);
		model.addAttribute("title", enterprise.getName());
		return "enterprise/product/products";
	}
	
	@PostMapping("product/search")
	public String searchProduct(@RequestParam("product") String name, 
			@RequestParam("enterpriseId") Long enterpriseId,
			Locale locale, Authentication auth, 
			RedirectAttributes flash) {
		User user = (User) userService.findByUsername(auth.getName());

		Enterprise enterprise = null;
		try {
			enterprise = enterpriseService.findById(enterpriseId);
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
		
		flash.addFlashAttribute("search", 
				productService.findByName(name, enterprise.getId()));
		
		return "redirect:/enterprise/" + enterprise.getId() + "/products";
	}
	
	@GetMapping("/{id}/add_products")
	public String addProduct(@PathVariable Long id, Model model,
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
		model.addAttribute("product", new Product());
		return "enterprise/product/add_product";
	}
	
	@PostMapping("/add_product")
	public String addProduct(@Valid Product product, BindingResult result,
			@RequestParam("img") MultipartFile img,
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
			return "enterprise/product/add_product";
		}
		
		if (!img.isEmpty()) {			
			try {
				byte[] image = img.getBytes();
				product.setImage(image);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		enterprise.addProduct(product);
		productService.save(product);
		flash.addFlashAttribute("enterprise", enterprise);
		flash.addFlashAttribute("success", 
				messageSource.getMessage("text.product.alert.success.create", null, locale));
		return "redirect:/enterprise/" + enterprise.getId();
	}
	
	@GetMapping("/{id}/edit_product/{product_id}")
	public String editProduct(@PathVariable Long id, @PathVariable Long product_id,
			Model model,Locale locale, Authentication auth, 
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
		
		Product product = null;
		try {
			product = productService.findById(product_id);
		} catch (Exception e) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.product.error.not.found", null, locale));
			e.printStackTrace();
			return "redirect:/";
		}

		model.addAttribute("user", user);
		model.addAttribute("title", enterprise.getName());
		model.addAttribute("product", product);
		return "enterprise/product/edit_product";
	}
	
	@PostMapping("/edit_product")
	public String editProduct(@RequestParam("longName") String longName,
			@RequestParam("shortName") String shortName,
			@RequestParam("price") Float price,
			@RequestParam("img") MultipartFile img,
			@RequestParam("enterprise") Long enterprise_id,
			@RequestParam("product") Long product_id,
			Model model, Locale locale,
			Authentication auth,
			RedirectAttributes flash) {
		User user = (User) userService.findByUsername(auth.getName());
		
		Enterprise enterprise = null;
		try {
			enterprise = enterpriseService.findById(enterprise_id);
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
		
		Product product = null;
		try {
			product = productService.findById(product_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!productService.isProductBelongsToEnterprise(product, enterprise)) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.enterprise.product.error.not.mismath", null, locale));
			return "redirect:/";
		}

		if (!img.isEmpty()) {			
			try {
				byte[] image = img.getBytes();
				product.setImage(image);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		product.setLongName(longName);
		product.setShortName(shortName);
		product.setPrice(price);
		productService.save(product);
		flash.addFlashAttribute("enterprise", enterprise);
		flash.addFlashAttribute("success", 
				messageSource.getMessage("text.product.alert.success.edit", null, locale));
		return "redirect:/enterprise/" + enterprise.getId();
	}
	
	@GetMapping("/product/img/view/{id}")
	public void view(@PathVariable Long id, HttpServletResponse response) throws ServletException, 
		IOException {
		Product product = null;
		
		try {
			product = productService.findById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(product.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(product.getImage());
			response.getOutputStream().close();
		} else {
			response.getOutputStream().close();
		}
	}
	
	@GetMapping(value = "/load_products/{term}/{enterprise_id}", produces = { "application/json" })
	public @ResponseBody List<Product> loadProducts(@PathVariable String term,
			@PathVariable Long enterprise_id) {
		List<Product> products = productService.findByName(term, enterprise_id);
		for(Product p : products) p.setEnterprise(null);
		return products;
	}
	
	@PostMapping("/product/delete")
	public String deleteProduct(@RequestParam("product") Long id,
			@RequestParam("password") String password,
			RedirectAttributes flash,
			Authentication auth,
			Locale locale) {
		User user = (User) userService.findByUsername(auth.getName());
		
		Product product;
		try {
			product = productService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.product.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, product.getEnterprise())) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		// Verificación del borrado.
		if(!passwordEncoder.matches(password, user.getPassword())) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.no.validation", null, locale));
			return "redirect:/enterprise/" + product.getEnterprise().getId() + "/products";
		}
		
		if(productService.isProductBelongsToSomeItemInvoice(product)) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.product.error.delete", null, locale));
			return "redirect:/enterprise/" + product.getEnterprise().getId() + "/products";
		}
		
		Enterprise e = product.getEnterprise();
		e.removeProduct(product.getId());
		productService.delete(product.getId());
		
		flash.addFlashAttribute("success", 
				messageSource.getMessage("text.product.alert.success.delete", null, locale));
		return "redirect:/enterprise/" + product.getEnterprise().getId() + "/products";
	}
	
	/***********************************************************************/
	//		INVOICES
	/***********************************************************************/
	
	@GetMapping("/costumer/{id}/invoice")
	public String issue(@PathVariable Long id, Model model,
			Locale locale, Authentication auth,
			RedirectAttributes flash) {
		User user = (User) userService.findByUsername(auth.getName());

		Client client = null;
		try {
			client = clientService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.client.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, client.getEnterprise())) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}

		model.addAttribute("invoice", new Invoice());
		model.addAttribute("client", client);
		model.addAttribute("user", user);
		model.addAttribute("title", client.getEnterprise().getName());
		return "enterprise/invoice/issue";
	}
	
	@PostMapping("/invoice/save")
	public String saveInvoice(@Valid Invoice invoice, 
			BindingResult result,
			@RequestParam("client") Long id,
			@RequestParam(name="item_id[]", required=false) Long[] itemId,
			@RequestParam(name="quantity[]", required=false) Integer[] quantity, 
			RedirectAttributes flash,
			Authentication auth,
			Locale locale) {
		User user = (User) userService.findByUsername(auth.getName());
		
		Client client = null;
		try {
			client = clientService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.client.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, client.getEnterprise())) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		if (result.hasErrors()) {
			return "redirect:enterprise/costumer/" + client.getId() + "/invoice";
		}

		if (itemId == null || itemId.length == 0) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.invoice.alert.error.issue", null, locale));
			return "redirect:/enterprise/costumer/" + client.getId() + "/invoice";
		}
		
		for (int i = 0; i < itemId.length; i++) {
			Product product = null;
			try {
				product = productService.findById(itemId[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}

			ItemInvoice line = new ItemInvoice();
			line.setQuantity(quantity[i]);
			line.setProduct(product);
			line.setPrice(product.getPrice());
			invoice.addItemFactura(line);
		}
		
		invoiceService.save(invoice);
		client.getInvoices().add(invoice);

		flash.addFlashAttribute("success", 
				messageSource.getMessage("text.invoice.alert.success.issue", null, locale));

		return "redirect:/";
	}
	
	@GetMapping("/invoice/{id}")
	public String showInvoice(@PathVariable Long id, Model model,
			Locale locale, Authentication auth,
			RedirectAttributes flash) {
		User user = (User) userService.findByUsername(auth.getName());
		
		Invoice invoice = null;
		try {
			invoice = invoiceService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.invoice.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}
		
		Client client = invoice.getClient();
		
		if(!enterpriseService.isEnterpriseBelongsToUser(user, client.getEnterprise())) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		model.addAttribute("user", user);
		model.addAttribute("invoice", invoice);
		model.addAttribute("title", client.getEnterprise().getName());
		return "enterprise/invoice/show_invoice";
	}

	@PostMapping("/invoice/delete")
	public String deleteInvoice(@RequestParam("invoice") Long id,
			@RequestParam("password") String password,
			RedirectAttributes flash,
			Authentication auth,
			Locale locale) {
		User user = (User) userService.findByUsername(auth.getName());
		
		Invoice invoice;
		try {
			invoice = invoiceService.findById(id);
		} catch (Exception ex) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.invoice.error.not.found", null, locale));
			System.out.println(ex.getMessage());
			return "redirect:/";
		}

		if(!enterpriseService.isEnterpriseBelongsToUser(user, invoice.getClient().getEnterprise())) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.enterprise.error.not.mismath", null, locale));
			return "redirect:/";
		}
		
		
		// Verificación del borrado.
		if(!passwordEncoder.matches(password, user.getPassword())) {
			flash.addFlashAttribute("error", 
					messageSource.getMessage("text.user.no.validation", null, locale));
			return "redirect:/enterprise/client/" + invoice.getClient().getId();
		}

		Client client = invoice.getClient();
		
		itemInvoiceService.deleteByInvoice(invoice.getId());
		invoiceService.delete(invoice.getId());
		
		flash.addFlashAttribute("success", 
				messageSource.getMessage("text.invoice.alert.success.delete", null, locale));
		return "redirect:/enterprise/client/" + client.getId();
	}

}
