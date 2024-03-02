package com.altqart.repository;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.BankType;

public interface BankTypeRepository extends CrudRepository<BankType, Integer> {

	public BankType getBankTypeByKey(String bankType);

}
