package com.altqart.req.model;

import com.altqart.model.Color;
import com.altqart.model.Product;
import com.altqart.model.Size;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantReq {

	private String id;

	private int color;

	private int size;

	private double price;

	private double qty;

	private double dicountPrice;

	private String sku;

	private String barCode;

	private String imageUrl;

	private String freeItems;

	private boolean dicount;

	private boolean available;

}
