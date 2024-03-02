package com.altqart.services;

import java.util.List;

import com.altqart.model.Bank;
import com.altqart.req.model.BankReq;
import com.altqart.resp.model.RespBank;
import com.altqart.resp.model.RespBankType;

public interface BankServices {

	public List<RespBank> getAllBanks(int start, int size);

	public RespBank getBankByPublicId(String id);

	public boolean update(BankReq bankReq);

	public boolean save(BankReq bankReq);
	
	public Bank getBankById(String publicId);

	public Bank getBankById(int bank);

	public List<RespBankType> getAllBankType();

}
