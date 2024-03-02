package com.altqart.mapper.impl;

import org.springframework.stereotype.Service;

import com.altqart.mapper.WarrantyMapper;
import com.altqart.model.Warranty;
import com.altqart.resp.model.RespWarranty;


@Service
public class WarrantyMapperImpl implements WarrantyMapper{

	@Override
	public RespWarranty mapRespWarranty(Warranty warranty) {

		if(warranty != null) {
			RespWarranty respWarranty = new RespWarranty();
			
			respWarranty.setId(warranty.getId());
			respWarranty.setTitle(warranty.getTitle());
			respWarranty.setValue(warranty.getValue());
			return respWarranty;
		}
		
		return null;
	}
}
