package com.altqart.mapper;

import java.util.List;

import com.altqart.model.BankBranch;
import com.altqart.req.model.BankBranchReq;
import com.altqart.resp.model.RespBankBranch;

public interface BankBranchMapper {

	public BankBranch mapBankBranch(BankBranchReq bankBranchReq);

	public List<RespBankBranch> mapAllRespBankBranch(List<BankBranch> bankBranchs);

	public RespBankBranch mapRespBankBranch(BankBranch bankBranch);

	public RespBankBranch mapRespBankBranchOnly(BankBranch bankBranch);
}
