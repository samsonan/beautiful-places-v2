package com.samsonan.bplaces.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;


/**
 * TODO: guava hashcode, equals, tostring
 * @author asamsonov@luxoft.com
 *
 */
@Entity
@Table(name="users")
public class User { 
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  
	
    @NotEmpty
	@Column(name="name")
    private String name;

    @NotEmpty
    @Email
	@Column(name="email")
    private String email;

	@Column(name="first_name")
    private String firstName; 

	@Column(name="last_name")
    private String lastName; 

    @NotEmpty
	@Column(name="password")
    private String password;

    @Transient
    private String confirmPassword;
    
	@Column(name="role")
    private String role;

	@Column(name="status")
    private Integer status;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isNew() {
		return (id == null);
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString(){
	    return MoreObjects.toStringHelper(this)
           .add("id", id)
	       .add("name", name)
           .add("email", email)
           .add("role", role)
           .add("first_name", lastName)
           .add("last_name", firstName)
           .add("status", status)
	       .toString();
	}

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final User other = (User) obj;
        return Objects.equal(this.name, other.name)
            && Objects.equal(this.email, other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name, this.email);
    }
} 
