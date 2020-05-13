package com.daw.facturapp.app.models.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
//import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="clients")
public class Client implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Size(min=9, max=9)
	@Pattern(regexp = "^[A-Za-z]?[0-9]{8}[A-Za-z]?$")
	@NotBlank						
	@Column
	private String nif;
	
	@NotBlank						
	@Column
	private String name;						
	
	@Column(name="created_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	
	@NotBlank
	@Column
	@Email
	private String email;

	@NotBlank
	@Pattern(regexp = "^[0-9]{9}$")
	@Column
	private String phone;
	
	@NotBlank
	@Column
	private String address;
	
	@ManyToOne
	@JoinColumn(name="enterprise_id")
	private Enterprise enterprise;
	
	@OneToMany(mappedBy="client", fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	private Set<Invoice> invoices;
	
	public Client() {
		this.invoices = new HashSet<Invoice>();
	}
	
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public Set<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(Set<Invoice> invoices) {
		this.invoices = invoices;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", nif=" + nif + ", name=" + name + ", createAt=" + createAt + ", email=" + email
				+ ", phone=" + phone + ", address=" + address + ", enterprise=" + enterprise + ", invoices=" + invoices
				+ "]";
	}

}
