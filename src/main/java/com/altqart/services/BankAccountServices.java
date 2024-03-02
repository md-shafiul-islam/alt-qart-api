package com.altqart.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.altqart.model.BankAccount;
import com.altqart.model.MethodAndTransaction;
import com.altqart.model.Store;
import com.altqart.req.model.BankAccountApprove;
import com.altqart.req.model.BankAccountReq;
import com.altqart.resp.model.RespBankAccount;


public interface BankAccountServices {

	public List<RespBankAccount> getAllBanksAccountByStatus(int i, int start, int size);

	public RespBankAccount getRespBanksAccountById(String id);

	public boolean update(BankAccountReq bankAccountReq);

	public boolean updateApprove(BankAccountApprove approve);

	public void save(BankAccountReq bankAccountReq, Map<String, Object> map);

	public BankAccount getBanksAccountById(String id);

	public void updateBankAccountViaTransaction(MethodAndTransaction methodAndTransaction, Session session, Date date);

	public void updateHaveAnyAccountTransactions(List<MethodAndTransaction> methodAndTransactions, Session session,
			Date date);

	public void saveBankAccountViaBusiness(BankAccountReq bankAccountReq, Store store, Map<String, Object> map);
	
	

}
