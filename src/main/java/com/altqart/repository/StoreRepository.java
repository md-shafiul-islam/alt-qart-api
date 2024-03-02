package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Store;

public interface StoreRepository extends CrudRepository<Store, Integer> {

	public Optional<Store> getStoreByPublicId(String storeId);

}
