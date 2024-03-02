package com.altqart.mapper;

import java.util.List;

import com.altqart.model.BankAccount;
import com.altqart.model.BankAccountType;
import com.altqart.model.BankDeposit;
import com.altqart.model.BankPaid;
import com.altqart.model.BankReceive;
import com.altqart.model.BankWithdraw;
import com.altqart.model.TempBankAccount;
import com.altqart.req.model.BankAccountReq;
import com.altqart.req.model.BankAccountTransactionReq;
import com.altqart.req.model.EsBankTransaction;
import com.altqart.req.model.RespBankDeposit;
import com.altqart.req.model.RespBankWithdraw;
import com.altqart.resp.model.RespAccount;
import com.altqart.resp.model.RespBankAccount;
import com.altqart.resp.model.RespBankAccountType;

public interface BankAccountMapper {

	public List<RespBankAccount> mapAllRespBankAccount(List<BankAccount> bankAccounts);

	public RespBankAccount mapRespBankAccount(BankAccount bankAccount);

	public BankAccount mapAccount(BankAccountReq bankAccountReq);

	public TempBankAccount mapTempBankAccount(BankAccountReq bankAccountReq);

	public List<RespBankAccount> mapAllRespBankAccountOnly(List<BankAccount> bankAccounts);
	
	public RespBankAccountType mapBankAccountTypeOnly(BankAccountType type);
	
	public RespBankAccount mapRespBankAccountOnly(BankAccount bankAccount);

	public List<RespBankWithdraw> mapAllRespBankWithdraw(List<BankWithdraw> bankWithdraws);

	public List<RespBankDeposit> mapAllRespBankDepositOnly(List<BankDeposit> bankAcDiposits);

	public List<RespBankWithdraw> mapAllRespBankWithdrawOnly(List<BankWithdraw> bankWithdraws);

	public BankDeposit mapBankDeposit(BankAccountTransactionReq bankAccountTransReq);

	public BankWithdraw mapBankWithdraw(BankAccountTransactionReq bankAccountTranReq);

	public BankReceive mapBankReceive(EsBankTransaction esBankTransaction);

	public BankPaid mapBankPaid(EsBankTransaction esBankTransaction);

	public RespBankAccount mapEsRespBankAccount(BankAccount bankAccount);

	public RespAccount mapEsRespAccount(BankAccount bankAccount);

	public List<RespBankAccount> mapAllBusinessRespBankAccount(List<BankAccount> bankAccounts);

}
