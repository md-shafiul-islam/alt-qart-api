package com.altqart.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="business_info")
public class StoreInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	@Column(name="public_id")
	private String publicId;
	
	@ManyToOne
	@JoinColumn(name="stakeholder", referencedColumnName = "id")
	private Stakeholder stakeholder;
	
	private String name;
	
	private String address, email, fax;
	
	@Column(name="phone_no")
	private String phoneNo;
	
	@Column(name="logo_url")
	private String logoUrl;
	
	private Date date;
	

}
