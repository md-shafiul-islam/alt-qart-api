package com.altqart.model;

import jakarta.persistence.Entity;
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
@Table(name = "shipping_address")
public class ShippingAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@ManyToOne
	@JoinColumn(name = "address", referencedColumnName = "id")
	private Address address;

	@ManyToOne
	@JoinColumn(name = "stakeholder", referencedColumnName = "id")
	private Stakeholder stakeholder;

	private String home;

	private String note;
}
