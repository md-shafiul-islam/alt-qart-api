package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.ParcelProvider;

public interface ParcelProviderRepository extends CrudRepository<ParcelProvider, Integer>{

	public Optional<ParcelProvider> getParcelProviderByKey(String key);

	public Optional<ParcelProvider> getParcelProviderByWebHookSecret(String givenKey);

}
