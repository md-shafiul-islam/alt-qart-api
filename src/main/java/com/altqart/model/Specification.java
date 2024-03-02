package com.altqart.model;

import jakarta.persistence.Column;
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
@Table(name = "specification")
public class Specification {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@ManyToOne
	@JoinColumn(name = "spc_key", referencedColumnName = "id")
	private SpecKey key;

	@Column(name = "value")
	private String value;

	@Column(name = "description")
	private String description;

	@ManyToOne
	@JoinColumn(name = "product", referencedColumnName = "id")
	private Product product;

	@Column(name = "is_key_feature")
	private boolean isFeature;
}
