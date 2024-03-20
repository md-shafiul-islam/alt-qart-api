package com.altqart.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "access_role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@Column(name = "gen_id")
	private String genId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
	private List<Access> accesses = new ArrayList<>();

	@JsonBackReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
	private List<User> users = new ArrayList<>();

	private String name;

	private String description;

	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "date_group")
	@Temporal(TemporalType.DATE)
	private Date dateGroupe;

	@Column(name = "auth_status")
	private int authStatus;

	private boolean supper;
}
