package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.BankAccountMapper;
import com.altqart.mapper.BankBranchMapper;
import com.altqart.model.Bank;
import com.altqart.model.BankAccount;
import com.altqart.model.BankAccountType;
import com.altqart.model.BankBranch;
import com.altqart.model.BankDeposit;
import com.altqart.model.BankPaid;
import com.altqart.model.BankReceive;
import com.altqart.model.BankTransection;
import com.altqart.model.BankWithdraw;
import com.altqart.model.TempBankAccount;
import com.altqart.req.model.BankAccountReq;
import com.altqart.req.model.BankAccountTransactionReq;
import com.altqart.req.model.EsBankTransaction;
import com.altqart.req.model.RespBankDeposit;
import com.altqart.req.model.RespBankWithdraw;
import com.altqart.resp.model.RespAccount;
import com.altqart.resp.model.RespBank;
import com.altqart.resp.model.RespBankAccount;
import com.altqart.resp.model.RespBankAccountType;
import com.altqart.resp.model.RespBankBranch;
import com.altqart.resp.model.RespBankTransection;
import com.altqart.services.BankBranchServices;
import com.altqart.services.StakeholderServices;

@Service
public class BankAccountMapperimpl implements BankAccountMapper {

	@Autowired
	private BankBranchServices bankBranchServices;

	@Autowired
	private BankBranchMapper bankBranchMapper;

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private StakeholderServices stakeholderServices;

	@Override
	public List<RespBankAccount> mapAllRespBankAccount(List<BankAccount> bankAccounts) {

		if (bankAccounts != null) {
			List<RespBankAccount> accounts = new ArrayList<>();
			for (BankAccount bankAccount : bankAccounts) {

				RespBankAccount account = mapRespBankAccountOnly(bankAccount);
				if (account != null) {
					accounts.add(account);
				}
			}

			return accounts;
		}

		return null;
	}

	@Override
	public RespBankAccount mapEsRespBankAccount(BankAccount bankAccount) {

		if (bankAccount != null) {
			RespBankAccount account = new RespBankAccount();
			account.setAccountNo(bankAccount.getAccountNo());
			account.setBankBranch(mapBankBranchOnly(bankAccount.getBankBranch()));
			account.setDate(bankAccount.getDate());
			account.setDateGroup(bankAccount.getDateGroup());
			account.setId(bankAccount.getPublicId());
			account.setName(bankAccount.getName());
			account.setName(bankAccount.getNote());

			return account;

		}

		return null;

	}

	@Override
	public BankAccount mapAccount(BankAccountReq bankAccountReq) {

		if (bankAccountReq != null) {
			BankAccount account = new BankAccount();

			account.setAccountNo(bankAccountReq.getAccountNo());

			account.setName(bankAccountReq.getName());
			account.setPublicId(helperServices.getGenPublicId());

			account.setNote(bankAccountReq.getNote());

			return account;

		}

		return null;
	}

	@Override
	public TempBankAccount mapTempBankAccount(BankAccountReq bankAccountReq) {

		if (bankAccountReq != null) {

			TempBankAccount account = new TempBankAccount();
			account.setAccountType(bankAccountReq.getAccountType());
			account.setActive(true);
			account.setName(bankAccountReq.getName());
			account.setRefId(bankAccountReq.getId());
			account.setMonthlyInstallment(bankAccountReq.getMonthlyInstallment());
			return account;
		}

		return null;
	}

	@Override
	public List<RespBankAccount> mapAllRespBankAccountOnly(List<BankAccount> bankAccounts) {

		if (bankAccounts != null) {

			List<RespBankAccount> accounts = new ArrayList<>();

			for (BankAccount bankAccount : bankAccounts) {
				RespBankAccount account = mapRespBankAccountOnly(bankAccount);

				if (account != null) {
					accounts.add(account);
				}
			}

			return accounts;
		}

		return null;
	}

	@Override
	public RespBankAccount mapRespBankAccount(BankAccount bankAccount) {

		RespBankAccount account = mapRespBankAccountOnly(bankAccount);
		if (account != null) {
			return account;
		}
		return null;
	}

	@Override
	public RespBankAccount mapRespBankAccountOnly(BankAccount bankAccount) {

		if (bankAccount != null) {

			RespBankAccount account = new RespBankAccount();
			account.setAccountNo(bankAccount.getAccountNo());
			account.setId(bankAccount.getPublicId());
			account.setName(bankAccount.getName());
			account.setType(mapBankAccountTypeOnly(bankAccount.getType()));
			account.setBankBranch(bankBranchMapper.mapRespBankBranchOnly(bankAccount.getBankBranch()));

			account.setCreditBalance(bankAccount.getCreditBalance());
			account.setDebitBalance(bankAccount.getDebitBalance());
			account.setTotalCredit(bankAccount.getTotalCredit());
			account.setTotalDebit(bankAccount.getTotalDebit());

			return account;
		}

		return null;
	}

	@Override
	public List<RespBankAccount> mapAllBusinessRespBankAccount(List<BankAccount> bankAccounts) {

		List<RespBankAccount> accounts = new ArrayList<>();

		for (BankAccount bankAccount : bankAccounts) {
			RespBankAccount account = mapRespBankAccountOnly(bankAccount);
			if (account != null) {
				if (bankAccount.isOwner()) {
					account.setOwner(1);
				}
				accounts.add(account);
			}
		}

		return accounts;
	}

	@Override
	public RespBankAccountType mapBankAccountTypeOnly(BankAccountType type) {

		if (type != null) {
			RespBankAccountType accountType = new RespBankAccountType();
			accountType.setDescription(type.getDescription());
			accountType.setName(type.getName());
			accountType.setValue(type.getValue());
			accountType.setId(type.getId());

			return accountType;
		}

		return null;
	}

	@Override
	public RespAccount mapEsRespAccount(BankAccount bankAccount) {

		if (bankAccount != null) {

			RespAccount account = new RespAccount();
			account.setAccountName(bankAccount.getName());
			account.setId(bankAccount.getPublicId());
			account.setAccountNo(bankAccount.getAccountNo());

			if (bankAccount.getBankBranch() != null) {
				if (bankAccount.getBankBranch().getBank() != null) {
					account.setBankName(bankAccount.getBankBranch().getBank().getName());
					account.setIconUrl(bankAccount.getBankBranch().getBank().getLogoUrl());
				}
			}

			return account;
		}

		return null;
	}

	public RespBankBranch mapBankBranchOnly(BankBranch bankBranch) {

		if (bankBranch != null) {
			RespBankBranch branch = new RespBankBranch();
			branch.setAddress(bankBranch.getAddress());
			branch.setBank(mapBankOnly(bankBranch.getBank()));
			branch.setName(bankBranch.getName());
			branch.setPhoneNo(bankBranch.getPhoneNo());

			return branch;

		}

		return null;
	}

	public RespBank mapBankOnly(Bank bank) {

		if (bank != null) {
			RespBank respBank = new RespBank();
			respBank.setDescription(bank.getDescription());
			respBank.setId(bank.getPublicId());
			respBank.setLogoUrl(bank.getLogoUrl());
			respBank.setName(bank.getName());

			return respBank;
		}

		return null;
	}

	@Override
	public BankWithdraw mapBankWithdraw(BankAccountTransactionReq bankAccountTranReq) {

		if (bankAccountTranReq != null) {

			BankWithdraw bankWithdraw = new BankWithdraw();
			Date date = new Date();
			bankWithdraw.setDate(date);
			bankWithdraw.setDateGroup(date);
			bankWithdraw.setAmount(bankAccountTranReq.getAmount());
			bankWithdraw.setChequeNo(bankAccountTranReq.getChequeNo());
			bankWithdraw.setNote(bankAccountTranReq.getNote());
			bankWithdraw.setPhoneNo(bankAccountTranReq.getRefPhoneNo());
			bankWithdraw.setPublicId(helperServices.getGenPublicId());
			bankWithdraw.setTransactionID(bankAccountTranReq.getTransId());
			bankWithdraw.setWithdrawnBy(bankAccountTranReq.getPostBy());

			return bankWithdraw;
		}
		return null;
	}

	@Override
	public List<RespBankDeposit> mapAllRespBankDepositOnly(List<BankDeposit> bankAcDiposits) {

		if (bankAcDiposits != null) {

			List<RespBankDeposit> deposits = new ArrayList<>();

			for (BankDeposit bankAcDeposit : bankAcDiposits) {

				RespBankDeposit acDeposit = mapBankDepositOnly(bankAcDeposit);

				if (acDeposit != null) {
					deposits.add(acDeposit);
				}
			}

			return deposits;
		}

		return null;
	}

	@Override
	public List<RespBankWithdraw> mapAllRespBankWithdraw(List<BankWithdraw> bankWithdraws) {

		if (bankWithdraws != null) {

			List<RespBankWithdraw> list = new ArrayList<>();

			for (BankWithdraw bankWithdraw : bankWithdraws) {

				RespBankWithdraw respBankWithdraw = mapRespBankWithdrawDetails(bankWithdraw);

				if (respBankWithdraw != null) {
					list.add(respBankWithdraw);
				}

			}

			return list;
		}

		return null;
	}

	@Override
	public List<RespBankWithdraw> mapAllRespBankWithdrawOnly(List<BankWithdraw> bankWithdraws) {

		if (bankWithdraws != null) {
			List<RespBankWithdraw> respBankWithdraws = new ArrayList<>();

			for (BankWithdraw bankWithdraw : bankWithdraws) {

				RespBankWithdraw withdraw = mapRespBankWithdrawOnly(bankWithdraw);

				if (withdraw != null) {
					respBankWithdraws.add(withdraw);
				}
			}

			return respBankWithdraws;
		}

		return null;
	}

	@Override
	public BankDeposit mapBankDeposit(BankAccountTransactionReq bankAccountTransReq) {

		if (bankAccountTransReq != null) {

			Date date = new Date();

			BankDeposit acDeposit = new BankDeposit();
			acDeposit.setAmount(bankAccountTransReq.getAmount());
			acDeposit.setDate(date);
			acDeposit.setDateGroup(date);
			acDeposit.setNote(bankAccountTransReq.getNote());
			acDeposit.setPhoneNo(bankAccountTransReq.getRefPhoneNo());
			acDeposit.setPublicId(helperServices.getGenPublicId());
			acDeposit.setRecepitNo(bankAccountTransReq.getReceptNo());
			acDeposit.setDepositedBy(bankAccountTransReq.getPostBy());

			return acDeposit;
		}

		return null;
	}

	@Override
	public BankPaid mapBankPaid(EsBankTransaction esBankTransaction) {

		if (esBankTransaction != null) {
			Date date = new Date();
			BankPaid bankPaid = new BankPaid();

			bankPaid.setAmount(esBankTransaction.getAmount());
			bankPaid.setChequeNo(esBankTransaction.getChequeNo());
			bankPaid.setDate(date);
			bankPaid.setDateGroup(date);
			bankPaid.setNote(esBankTransaction.getNote());
			bankPaid.setPhoneNo(esBankTransaction.getPhoneNo());
			bankPaid.setPublicId(helperServices.getGenPublicId());
			bankPaid.setPostedBy(esBankTransaction.getPostedBy());
			bankPaid.setReceiptNo(esBankTransaction.getReceiptNo());
			bankPaid.setTransactionID(esBankTransaction.getTransactionID());

			return bankPaid;

		}
		return null;
	}

	@Override
	public BankReceive mapBankReceive(EsBankTransaction esBankTransaction) {
		if (esBankTransaction != null) {
			Date date = new Date();
			BankReceive bankReceive = new BankReceive();

			bankReceive.setAmount(esBankTransaction.getAmount());
			bankReceive.setChequeNo(esBankTransaction.getChequeNo());
			bankReceive.setDate(date);
			bankReceive.setDateGroup(date);
			bankReceive.setNote(esBankTransaction.getNote());
			bankReceive.setPhoneNo(esBankTransaction.getPhoneNo());
			bankReceive.setPublicId(helperServices.getGenPublicId());
			bankReceive.setPostedBy(esBankTransaction.getPostedBy());
			bankReceive.setReceiptNo(esBankTransaction.getReceiptNo());
			bankReceive.setTransactionID(esBankTransaction.getTransactionID());

			return bankReceive;

		}
		return null;
	}

	private RespBankDeposit mapBankDepositOnly(BankDeposit bankAcDeposit) {

		if (bankAcDeposit != null) {
			RespBankDeposit deposit = new RespBankDeposit();
			deposit.setAmount(bankAcDeposit.getAmount());
			deposit.setApprove(bankAcDeposit.getApprove());
			deposit.setDate(bankAcDeposit.getDate());
			deposit.setDateGroup(bankAcDeposit.getDateGroup());
			deposit.setId(bankAcDeposit.getPublicId());
			deposit.setRecepitNo(bankAcDeposit.getRecepitNo());
			deposit.setDepositedBy(bankAcDeposit.getDepositedBy());
			deposit.setNote(bankAcDeposit.getNote());
			deposit.setPhoneNo(bankAcDeposit.getPhoneNo());
			deposit.setRecepitNo(bankAcDeposit.getRecepitNo());

			return deposit;
		}
		return null;
	}

	private RespBankWithdraw mapRespBankWithdrawOnly(BankWithdraw bankWithdraw) {

		if (bankWithdraw != null) {
			RespBankWithdraw respBankWithdraw = new RespBankWithdraw();
			respBankWithdraw.setAmount(bankWithdraw.getAmount());
			respBankWithdraw.setApprove(bankWithdraw.getApprove());
			respBankWithdraw.setChequeNo(bankWithdraw.getChequeNo());
			respBankWithdraw.setDate(bankWithdraw.getDate());
			respBankWithdraw.setDateGroup(bankWithdraw.getDateGroup());
			respBankWithdraw.setId(bankWithdraw.getPublicId());
			respBankWithdraw.setNote(bankWithdraw.getNote());
			respBankWithdraw.setPhoneNo(bankWithdraw.getPhoneNo());
			respBankWithdraw.setTransactionID(bankWithdraw.getTransactionID());
			respBankWithdraw.setWithdrawnBy(bankWithdraw.getWithdrawnBy());

			return respBankWithdraw;

		}

		return null;
	}

	private RespBankWithdraw mapRespBankWithdrawDetails(BankWithdraw bankWithdraw) {

		if (bankWithdraw != null) {
			RespBankWithdraw respBankWithdraw = mapRespBankWithdrawOnly(bankWithdraw);

			if (respBankWithdraw != null) {
				respBankWithdraw.setBankTransection(mapRespBankTransection(bankWithdraw.getBankTransection()));
			}

			return respBankWithdraw;

		}

		return null;
	}

	private RespBankTransection mapRespBankTransection(BankTransection bankTransection) {

		if (bankTransection != null) {

			RespBankTransection respBankTransection = new RespBankTransection();
			respBankTransection.setAmount(bankTransection.getAmount());
			respBankTransection.setDate(bankTransection.getDate());
			respBankTransection.setDateGroup(bankTransection.getDateGroup());
			respBankTransection.setId(bankTransection.getPublicId());
			respBankTransection.setPay(bankTransection.getPay());
			respBankTransection.setReceive(bankTransection.getReceive());
			respBankTransection.setToken(bankTransection.getToken());

			return respBankTransection;

		}
		return null;
	}

}
