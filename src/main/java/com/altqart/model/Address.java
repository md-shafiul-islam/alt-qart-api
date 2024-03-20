package com.altqart.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "address")
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "phone_no")
	private String phoneNo;

	@Column(name = "phone_no2")
	private String phoneNo2;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stakeholder", referencedColumnName = "id")
	private Stakeholder stakeholder;

	@ManyToOne
	@JoinColumn(name = "city", referencedColumnName = "id")
	private City city;

	@ManyToOne
	@JoinColumn(name = "zone", referencedColumnName = "id")
	private Zone zone;

	@ManyToOne
	@JoinColumn(name = "area", referencedColumnName = "id")
	private Area area;

	@ManyToOne
	@JoinColumn(name = "upazila", referencedColumnName = "id")
	private Upazila upazila;

	@Column(name = "full_address")
	private String fullAddress;

	@Column(name = "is_default")
	private boolean isDefault;

	@Column(name = "is_office")
	private boolean isOffice;

	@OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
	private List<ShippingAddress> shippingAddresses;

	@Column(name = "zip_code")
	private String zipCode;

}
