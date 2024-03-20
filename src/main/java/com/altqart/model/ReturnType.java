package com.altqart.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "return_type")
public class ReturnType {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "returnType")
	private List<SaleReturnInvoice> saleReturnInvoices = new ArrayList<>();

	private String name;

	private String value;

	private String description;

	public ReturnType(int id, String name, String value) {
		this.id = id;
		this.name = name;
		this.value = value;
	}

}
