package com.altqart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "cart_item")
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@ManyToOne
	@JoinColumn(name = "product", referencedColumnName = "id")
	private Product product;

	@OneToOne
	@JoinColumn(name = "variant", referencedColumnName = "id")
	private Variant variant;

	@ManyToOne
	@JoinColumn(name = "cart", referencedColumnName = "id")
	private Cart cart;
	
	private double qty;
	
	private double price;

	@Column(name = "sub_total")
	private double subTotal;

	@Column(name="discount_price")
	private double discountPrice;
	
	@Column(name="is_choose")
	private boolean isChoose = false;

}
