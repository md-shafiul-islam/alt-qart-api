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
@Table(name = "product_price")
public class ProductPrice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column
	private String title;

	@Column
	private String type;

	@Column(name = "price")
	private double price;

	@Column(name = "discount_price")
	private double discountPrice;

	@Column(name = "discount_status")
	private boolean isDiscounted;

	@ManyToOne
	@JoinColumn(name = "product", referencedColumnName = "id")
	private Product product;
}
