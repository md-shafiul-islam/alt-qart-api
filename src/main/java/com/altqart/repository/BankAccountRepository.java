package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.BankAccount;

public interface BankAccountRepository extends CrudRepository<BankAccount, Integer>{

	public Optional<BankAccount> getBankAccountByAccountNo(String accountNo);

	public Optional<BankAccount> getBankAccountByPublicId(String id);

}
