package com.altqart.services;

import java.util.List;

import com.altqart.req.model.BankAccountTypeReq;
import com.altqart.resp.model.RespBank;
import com.altqart.resp.model.RespBankAccountType;

public interface BankAccountTypeServices {

	public List<RespBankAccountType> getAllBankAccountTypes();

	public RespBank getBankAccountTypeById(String id);

	public boolean save(BankAccountTypeReq bankAccountTypeReq);

	public boolean update(BankAccountTypeReq bankAccountTypeReq);

}
