package com.altqart.mapper;

import java.util.List;

import com.altqart.model.BankAccountType;
import com.altqart.resp.model.RespBankAccountType;


public interface BankAccountTypeMapper {

	public List<RespBankAccountType> mapAllRespBankAccountType(List<BankAccountType> accountTypes);
	
	public RespBankAccountType mapRespBankAccountTypeOnly(BankAccountType accountType);

}
