package com.altqart.resp.model;

import com.altqart.model.Color;
import com.altqart.model.Product;
import com.altqart.model.Size;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespMinVariant {

	private String id;

	private RespProduct product;

	private String color;

	private String size;

	private double price;

	private double qty;

	private double dicountPrice;

	private String sku;

	private String barCode;

	private String imageUrl;

	private String freeItems;

	private boolean dicount;

	private boolean available;

	private boolean isFeature;

}
