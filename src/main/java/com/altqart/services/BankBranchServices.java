package com.altqart.services;

import java.util.List;

import com.altqart.model.BankBranch;
import com.altqart.req.model.BankBranchReq;
import com.altqart.resp.model.RespBankBranch;

public interface BankBranchServices {

	public BankBranch getBankBranchByKey(String branch);

	public List<RespBankBranch> getAllBankBranchs();

	public RespBankBranch getBankById(int id);

	public boolean update(BankBranchReq bankBranchReq);

	public boolean save(BankBranchReq bankBranchReq);

	public BankBranch getBankBranchById(int branch);

}
