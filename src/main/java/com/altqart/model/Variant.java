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
@Table(name = "variant")
public class Variant {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@Column(name = "public_id")
	private String publicId;

	@ManyToOne
	@JoinColumn(name = "product", referencedColumnName = "id")
	private Product product;

	@ManyToOne
	@JoinColumn(name="color", referencedColumnName = "id")
	private Color color;

	@ManyToOne
	@JoinColumn(name = "size", referencedColumnName = "id")
	private Size size;

	private double price;

	private double qty;

	@Column(name = "dicount_price")
	private double dicountPrice;

	private String sku;

	@Column(name = "bar_code")
	private String barCode;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "free_items")
	private String freeItems;

	private boolean dicount;

	private boolean available;
	
	@Column(name="feature")
	private boolean isFeature;

}
