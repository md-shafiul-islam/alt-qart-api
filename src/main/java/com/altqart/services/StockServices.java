package com.altqart.services;

import java.util.List;

import com.altqart.model.Product;

public interface StockServices {

	public List<Product> getProductsByItemId(int itemId);
	
	public List<Product> getAllProduct();

	public double getProductStockPrice();

}
