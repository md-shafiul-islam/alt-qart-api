package com.altqart.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.model.TempStakeholder;
import com.altqart.repository.TempStakeholderRepository;
import com.altqart.services.TempStakeholderServices;

@Service
public class TempStakeholderServicesImpl implements TempStakeholderServices{

	@Autowired
	private TempStakeholderRepository tempStakeholderRepository;
	
	@Override
	public TempStakeholder getActiveTempStatkeholderByPublicId(String publicId) {
		
		Optional<TempStakeholder> optional = tempStakeholderRepository.getTempStakeholderByPublicIdAndActive(publicId, true);
		
		if(optional.isPresent() && !optional.isEmpty()) {
			return optional.get();
		}
		
		return null;
	}
}
