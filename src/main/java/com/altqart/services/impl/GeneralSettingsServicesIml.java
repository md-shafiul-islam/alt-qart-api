package com.altqart.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.model.GeneralSettings;
import com.altqart.repository.GeneralSettingsRespoitory;
import com.altqart.services.GeneralSettingsServices;

@Service
public class GeneralSettingsServicesIml implements GeneralSettingsServices {

	@Autowired
	private GeneralSettingsRespoitory generalSettingsRespoitory;

	@Override
	public GeneralSettings getSettings() {
		Optional<GeneralSettings> option = generalSettingsRespoitory.findById(1);

		if (option.isPresent() && !option.isEmpty()) {
			return option.get();
		}

		return null;
	}
}
