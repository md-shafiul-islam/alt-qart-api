package com.altqart.repository;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.BankDeposit;

public interface BankDepositRepository extends CrudRepository<BankDeposit, Integer> {

}
