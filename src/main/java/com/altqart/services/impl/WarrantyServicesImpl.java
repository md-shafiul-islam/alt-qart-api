package com.altqart.services.impl;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.model.Warranty;
import com.altqart.repository.WarrantyRepository;
import com.altqart.services.WarrantyServices;

@Service
public class WarrantyServicesImpl implements WarrantyServices{

	
	@Autowired
	private WarrantyRepository warrantyRepository;
	
	@Override
	public Warranty getWarrantyById(int id) {
		
		if(id > 0) {
			Optional<Warranty> option = warrantyRepository.findById(id);
			
			if(option.isPresent() &&  !option.isEmpty()) {
				return option.get();
			}
		}
		
		return null;
	}
	
	@Override
	public List<Warranty> getWarranties() {
		return (List<Warranty>) warrantyRepository.findAll();
	}
}
