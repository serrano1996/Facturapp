package com.daw.facturapp.app.models.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name="enterprises")
public class Enterprise implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Pattern(regexp = "^[A-Za-z]{1}[0-9]{8}$")
	@NotBlank						
	@Column
	private String cif;
	
	@NotBlank								
	@Column
	private String name;
	
	@NotBlank								
	@Column
	private String address;
	
	@NotBlank								
	@Column
	private String province;
	
	@NotBlank							
	@Column
	private String town;
	
	@NotBlank								
	@Column
	private String country;
	
	@NotBlank	
	@Email
	@Column
	private String email_contact;
	
	@Pattern(regexp = "^[0-9]{9}$")
	@NotBlank								
	@Column
	private String phone_contact;
	
	@Lob
	@Column
	private byte[] logo;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.PERSIST)
	@JoinColumn(name="enterprise_id")
	//@OneToMany(mappedBy="enterprise", fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	private Set<Client> clients;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.PERSIST)
	@JoinColumn(name = "enterprise_id")
	//@OneToMany(mappedBy="enterprise", fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	private Set<Product> products;
	
	public Enterprise() {
		this.clients = new HashSet<Client>();
		this.products = new HashSet<Product>();
	}
	
	public void addClient(Client client) {
		this.clients.add(client);
	}
	
	public void removeClient(Long id) {
        Iterator<Client> it = this.clients.iterator();
        while(it.hasNext()) {
        	if(it.next().getId() == id) {
        		it.remove();
        	}
        }
    } 
	
	public void addProduct(Product product) {
		this.products.add(product);
	}
	
	public void removeProduct(Long id) {
        Iterator<Product> it = this.products.iterator();
        while(it.hasNext()) {
        	if(it.next().getId() == id) {
        		it.remove();
        	}
        }
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail_contact() {
		return email_contact;
	}

	public void setEmail_contact(String email_contact) {
		this.email_contact = email_contact;
	}

	public String getPhone_contact() {
		return phone_contact;
	}

	public void setPhone_contact(String phone_contact) {
		this.phone_contact = phone_contact;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public Set<Client> getClients() {
		return clients;
	}

	public void setClients(Set<Client> clients) {
		this.clients = clients;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Enterprise [id=" + id + ", cif=" + cif + ", name=" + name + ", address=" + address + ", province="
				+ province + ", town=" + town + ", country=" + country + ", email_contact=" + email_contact
				+ ", phone_contact=" + phone_contact + ", logo=" + Arrays.toString(logo) + ", clients=" + clients
				+ ", products=" + products + "]";
	}

}
