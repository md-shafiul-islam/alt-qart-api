package com.altqart.repository;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Address;

public interface AddressRepository extends CrudRepository<Address, Integer> {

}
