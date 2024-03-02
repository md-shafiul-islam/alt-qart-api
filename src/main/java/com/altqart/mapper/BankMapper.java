package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Bank;
import com.altqart.model.BankType;
import com.altqart.req.model.BankReq;
import com.altqart.resp.model.RespBank;
import com.altqart.resp.model.RespBankType;

public interface BankMapper {

	public Bank mapBank(BankReq bankReq);

	public List<RespBank> mapAllRespBank(List<Bank> banks);

	public RespBank mapRespBank(Bank bank);

	public RespBank mapBankOnly(Bank bank);

	public List<RespBankType> mapAllRespBankType(List<BankType> bankTypes);

}
