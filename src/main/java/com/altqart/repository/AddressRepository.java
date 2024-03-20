package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Address;


public interface AddressRepository extends CrudRepository<Address, Integer> {

	public Optional<Address> getAddressByPublicId(String address);

}
