package com.daw.facturapp.app.models.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
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

@Entity
@Table(name="enterprises")
public class Enterprise implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
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
	
	@NotBlank								
	@Column
	private String phone_contact;
	
	@Lob
	@Column
	private byte[] logo;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.PERSIST)
	@JoinColumn(name="enterprise_id")
	private Set<Client> clients;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.PERSIST)
	@JoinColumn(name = "enterprise_id")
	private Set<Product> products;
	
	public Enterprise() {
		this.clients = new HashSet<Client>();
		this.products = new HashSet<Product>();
	}
	
	public void addClient(Client client) {
		this.clients.add(client);
	}
	
	public void addProduct(Product product) {
		this.products.add(product);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((cif == null) ? 0 : cif.hashCode());
		result = prime * result + ((clients == null) ? 0 : clients.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((email_contact == null) ? 0 : email_contact.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + Arrays.hashCode(logo);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phone_contact == null) ? 0 : phone_contact.hashCode());
		result = prime * result + ((products == null) ? 0 : products.hashCode());
		result = prime * result + ((province == null) ? 0 : province.hashCode());
		result = prime * result + ((town == null) ? 0 : town.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Enterprise other = (Enterprise) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (cif == null) {
			if (other.cif != null)
				return false;
		} else if (!cif.equals(other.cif))
			return false;
		if (clients == null) {
			if (other.clients != null)
				return false;
		} else if (!clients.equals(other.clients))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (email_contact == null) {
			if (other.email_contact != null)
				return false;
		} else if (!email_contact.equals(other.email_contact))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (!Arrays.equals(logo, other.logo))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phone_contact == null) {
			if (other.phone_contact != null)
				return false;
		} else if (!phone_contact.equals(other.phone_contact))
			return false;
		if (products == null) {
			if (other.products != null)
				return false;
		} else if (!products.equals(other.products))
			return false;
		if (province == null) {
			if (other.province != null)
				return false;
		} else if (!province.equals(other.province))
			return false;
		if (town == null) {
			if (other.town != null)
				return false;
		} else if (!town.equals(other.town))
			return false;
		return true;
	}

}
