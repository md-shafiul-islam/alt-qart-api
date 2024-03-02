package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.BankBranch;

public interface BankBranchRepository extends CrudRepository<BankBranch, Integer> {

	public Optional<BankBranch> getBankBranchByKey(String branch);

	public Optional<BankBranch> findById(int branch);

}
