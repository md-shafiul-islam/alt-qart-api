package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Bank;

public interface BankRepository extends CrudRepository<Bank, Integer> {

	public Optional<Bank> getBankByPublicId(String publicId);

}
