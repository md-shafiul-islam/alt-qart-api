package com.altqart.repository;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.BankAccount;

public interface BankAccountTypeRepository  extends CrudRepository<BankAccount, Integer>{

}
