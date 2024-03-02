package com.altqart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "access")
public class Access {


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	@Column(name = "public_id")
	private String publicId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "access_type", referencedColumnName = "id")
	private AccessType accessType;
	
	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	private Role role;
	
	private String name;
	
	private String description;
	
	@Column(name = "read_acc")
	private boolean view;
	
	@Column(name = "zero_access")
	private boolean noAccess;
	
	@Column(name = "add_acc")
	private boolean add;
	
	@Column(name = "edit_acc")
	private boolean edit;
	
	@Column(name = "add_approve")
	private boolean approve;
	
	@Column(name = "edit_approve")
	private boolean updateApproval;
	
	@Column(name = "all_access")
	private boolean all;
}
