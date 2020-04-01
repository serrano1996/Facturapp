package com.daw.facturapp.app.models.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name="users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank					// El campo no puede estar vacio.
	@Size(min=4, max=45)		// La longitud de caracteres del campo.
	@Column(unique=true)
	private String username;
	
	@NotBlank					
	@Size(min=4)
	@Column
	private String password;
	
	@NotBlank					
	@Size(min=4, max=45)
	@Column
	private String name;
	
	@NotBlank					
	@Size(min=3, max=45)
	@Column
	private String lastname;
	
	@NotBlank					
	@Email						// Formato incorrecto.
	@Column(unique=true)
	private String email;
	
	@Column(name="created_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	
	@NotBlank
	@Transient // Indica que este campo no es persistente, no se guarda en la BD.
	private String confirmPassword;
	
	@ManyToMany(fetch=FetchType.LAZY) // Relación N:M.
	@JoinTable(
			name="users_authorities", // Nombre de la tabla N:M.
			joinColumns=@JoinColumn(name="user_id"), // Nombre del campo que guarda el usuario.
			inverseJoinColumns=@JoinColumn(name="role_id") // Nombre del campo que guarda el rol.
	)
	private Set<Role> roles;
	
	@Column(name="active", columnDefinition="TINYINT(1)")
	private Boolean enabled;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.PERSIST)
	@JoinColumn(name="user_id")
	private Set<Enterprise> enterprises;
	
	public User() {
		super();
		this.roles = new HashSet<Role>();
		this.enterprises = new HashSet<Enterprise>();
	}

	public User(Long id) {
		this.id = id;
	}
	
	/**
	 * Se ejecuta antes de persistir el objeto.
	 */
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
		this.enabled = true;
	}
	
	public void removeEnterprise(String name) {
        Iterator<Enterprise> it = this.enterprises.iterator();
        while(it.hasNext()) {
        	if(it.next().getName().equals(name)) {
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Enterprise> getEnterprises() {
		return enterprises;
	}

	public void setEnterprises(Set<Enterprise> enterprises) {
		this.enterprises = enterprises;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", lastname="
				+ lastname + ", email=" + email + ", createAt=" + createAt + ", confirmPassword=" + confirmPassword
				+ ", roles=" + roles + ", enabled=" + enabled + ", enterprises=" + enterprises + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((confirmPassword == null) ? 0 : confirmPassword.hashCode());
		result = prime * result + ((createAt == null) ? 0 : createAt.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((enterprises == null) ? 0 : enterprises.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (confirmPassword == null) {
			if (other.confirmPassword != null)
				return false;
		} else if (!confirmPassword.equals(other.confirmPassword))
			return false;
		if (createAt == null) {
			if (other.createAt != null)
				return false;
		} else if (!createAt.equals(other.createAt))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled == null) {
			if (other.enabled != null)
				return false;
		} else if (!enabled.equals(other.enabled))
			return false;
		if (enterprises == null) {
			if (other.enterprises != null)
				return false;
		} else if (!enterprises.equals(other.enterprises))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
}