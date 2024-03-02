package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.BankBranchMapper;
import com.altqart.mapper.BankMapper;
import com.altqart.model.Bank;
import com.altqart.model.BankBranch;
import com.altqart.req.model.BankBranchReq;
import com.altqart.resp.model.RespBank;
import com.altqart.resp.model.RespBankBranch;
import com.altqart.services.BankServices;

@Service
public class BankBranchMapperImpl implements BankBranchMapper {

	@Autowired
	private BankServices bankServices;

	@Autowired
	private BankMapper bankMapper;

	@Override
	public BankBranch mapBankBranch(BankBranchReq bankBranchReq) {

		if (bankBranchReq != null) {

			Bank bank = bankServices.getBankById(bankBranchReq.getBank());

			if (bank != null) {
				BankBranch bankBranch = new BankBranch();
				bankBranch.setAddress(bankBranchReq.getAddress());
				bankBranch.setBank(bank);
				bankBranch.setKey(bankBranchReq.getKey());
				bankBranch.setName(bankBranchReq.getName());
				bankBranch.setPhoneNo(bankBranchReq.getPhoneNo());
				return bankBranch;
			}

		}

		return null;
	}

	@Override
	public List<RespBankBranch> mapAllRespBankBranch(List<BankBranch> bankBranchs) {

		if (bankBranchs != null) {

			List<RespBankBranch> branchs = new ArrayList<>();

			for (BankBranch bankBranch : bankBranchs) {
				RespBankBranch branch = mapRespBankBranchOnly(bankBranch);

				if (branch != null) {
					branchs.add(branch);
				}
			}

			return branchs;
		}

		return null;
	}

	@Override
	public RespBankBranch mapRespBankBranch(BankBranch bankBranch) {

		if (bankBranch != null) {
			RespBankBranch branch = mapRespBankBranchOnly(bankBranch);

			if (branch != null) {
				RespBank bank = bankMapper.mapBankOnly(bankBranch.getBank());
				branch.setBank(bank);
			}

			return branch;
		}

		return null;
	}

	@Override
	public RespBankBranch mapRespBankBranchOnly(BankBranch bankBranch) {

		if (bankBranch != null) {
			RespBankBranch branch = new RespBankBranch();

			branch.setAddress(bankBranch.getAddress());
			branch.setId(bankBranch.getId());
			branch.setName(bankBranch.getName());
			branch.setPhoneNo(bankBranch.getPhoneNo());
			
			branch.setBank(mapBankOnly(bankBranch.getBank()));
			return branch;
		}

		return null;
	}

	private RespBank mapBankOnly(Bank bank) {
		
		if(bank != null) {
			RespBank respBank = new RespBank();
			respBank.setDescription(bank.getDescription());
			respBank.setId(bank.getPublicId());
			respBank.setLogoUrl(bank.getLogoUrl());
			respBank.setName(bank.getName());
			
			return respBank;
		}
		
		return null;
	}

}
