package com.altqart.services;

import java.util.List;

import com.altqart.model.Warranty;

public interface WarrantyServices {

	public Warranty getWarrantyById(int id);

//
	public List<Warranty> getWarranties();

}
