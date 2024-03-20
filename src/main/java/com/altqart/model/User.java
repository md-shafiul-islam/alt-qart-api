package com.altqart.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.altqart.model.security.Authority;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {

	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "pub_id")
	private String publicId;

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private Store store;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
	private List<Order> orders = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
	private List<Paid> paids = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "approveUser", fetch = FetchType.LAZY)
	private List<SaleReturnInvoice> saleReturnInvoices = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
	private List<Credential> credentials = new ArrayList<>();

	@OneToOne(mappedBy = "user")
	private Stakeholder stakeholder;

	@Column(name = "name")
	private String name;

	@Column(name = "user_name")
	private String username;

	@Column(name = "phone_no")
	private String phoneNo;

	@Column(name = "email")
	private String email;

	@Column(name = "palec_token")
	private String placeToken;

	@Column(name = "auth_user")
	private String authUser;

	@Column(name = "generated_id")
	private String generatedId;

	@Column(name = "code")
	private String code;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_date")
	private Date date;

	@Temporal(TemporalType.DATE)
	@Column(name = "update_date")
	private Date udate;

	@JsonManagedReference
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role", referencedColumnName = "id")
	private Role role;

	@Column(name = "enabled_status")
	private int enabled;

	@Column(name = "locked_status")
	private int locked;

	@Getter(AccessLevel.NONE)
	@Column(name = "credential_exp_status")
	private int credentialsNonExpired;

	@Column(name = "account_exp_status")
	private int accountNonExpired;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new Authority(role.getName()));
		return authorities;
	}

	@Override
	public String getUsername() {

		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		if (accountNonExpired == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		if (locked == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		if (credentialsNonExpired == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isEnabled() {
		if (enabled == 1) {
			return true;
		}
		return false;
	}

	@Override
	public String getPassword() {

		for (Credential credential : credentials) {
			if (credential.getStatus() == 1) {
				return credential.getPassword();
			}
		}

		return null;
	}

}
