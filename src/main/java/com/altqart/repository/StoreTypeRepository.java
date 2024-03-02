package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.StoreType;

public interface StoreTypeRepository extends CrudRepository<StoreType, Integer> {

	public Optional<StoreType> getStoreTypeByValue(String storeType);

}
