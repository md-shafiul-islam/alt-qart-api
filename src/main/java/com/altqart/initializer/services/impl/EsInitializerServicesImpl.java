package com.altqart.initializer.services.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperAuthenticationServices;
import com.altqart.helper.services.HelperDateServices;
import com.altqart.helper.services.HelperServices;
import com.altqart.initializer.services.EsInitializerServices;
import com.altqart.model.BankAccount;
import com.altqart.model.BankAccountCredit;
import com.altqart.model.BankAccountCreditRecord;
import com.altqart.model.BankAccountDebit;
import com.altqart.model.BankAccountDebitRecord;
import com.altqart.model.BankDeposit;
import com.altqart.model.BankTransection;
import com.altqart.model.BankWithdraw;
import com.altqart.model.Cart;
import com.altqart.model.EsCredit;
import com.altqart.model.EsCreditDetail;
import com.altqart.model.EsDebit;
import com.altqart.model.EsDebitDetail;
import com.altqart.model.Stakeholder;
import com.altqart.model.User;

@Service
public class EsInitializerServicesImpl implements EsInitializerServices {

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperDateServices dateServices;

	@Autowired
	private HelperAuthenticationServices authenticationServices;

	@Override
	public EsCreditDetail initEsCreditDetail(EsCredit esCredit, Date date) {

		EsCreditDetail creditDetail = initCreditDetail(date);

		creditDetail.setCredit(esCredit);
		return creditDetail;
	}

	@Override
	public EsDebitDetail initEsDebitDetail(EsDebit esDebit, Date date) {

		EsDebitDetail debitDetail = initDebitDetail(date);
		debitDetail.setDebit(esDebit);
		return debitDetail;
	}

	@Override
	public EsCredit initEsCreditByStakeholder(Stakeholder stakeholder, Date date) {

		EsCredit credit = new EsCredit();
		credit.setStakeholder(stakeholder);
		credit.setDate(date);
		credit.setPublicId(helperServices.getGenPublicId());
		credit.setNote(stakeholder.getName() + " Account Credit side");
		return credit;
	}

	@Override
	public EsDebit initEsDebitByStakeholder(Stakeholder stakeholder, Date date) {
		EsDebit esDebit = new EsDebit();

		esDebit.setStakeholder(stakeholder);
		esDebit.setDate(date);
		esDebit.setNote(stakeholder.getName() + " Account Debit side");
		esDebit.setPublicId(helperServices.getGenPublicId());

		return esDebit;
	}

	@Override
	public EsDebit initEsDebitBySupplier(Stakeholder stakeholder, Date date) {

		EsDebit debit = new EsDebit();
		debit.setDate(date);
		if (stakeholder != null) {
			debit.setNote(stakeholder.getName() + " Account Debit side");
			debit.setStakeholder(stakeholder);
		}
		debit.setPublicId(helperServices.getGenPublicId());

		return debit;
	}

	@Override
	public EsCredit initEsCreditBySupplier(Stakeholder stakeholder, Date date) {

		EsCredit credit = new EsCredit();
		credit.setDate(date);
		if (stakeholder != null) {
			credit.setNote(stakeholder.getName() + " Account Credit side");
			credit.setStakeholder(stakeholder);
		}
		credit.setPublicId(helperServices.getGenPublicId());
		return credit;
	}

	private EsCreditDetail initCreditDetail(Date date) {
		EsCreditDetail creditDetail = new EsCreditDetail();
		creditDetail.setDate(date);
		creditDetail.setDateGroup(date);
		creditDetail.setPublicId(helperServices.getGenPublicId());
		creditDetail.setStatus(1);
		return creditDetail;
	}

	private EsDebitDetail initDebitDetail(Date date) {
		EsDebitDetail debitDetail = new EsDebitDetail();
		debitDetail.setDate(date);
		debitDetail.setDateGroup(date);
		debitDetail.setStatus(1);
		debitDetail.setUser(authenticationServices.getCurrentUser());
		return debitDetail;
	}

	@Override
	public BankAccountCredit initBankAccountCredit(BankAccount account, Date date) {
		BankAccountCredit credit = new BankAccountCredit();
		credit.setBankAccount(account);
		credit.setDate(date);
		credit.setPublicId(helperServices.getGenPublicId());
		credit.setDescription(account.getName() + " Bank Account Credit side");
		credit.setUser(authenticationServices.getCurrentUser());
		return credit;
	}

	@Override
	public BankAccountDebit initBankAccountDebit(BankAccount account, Date date) {
		BankAccountDebit debit = new BankAccountDebit();
		debit.setBankAccount(account);
		debit.setDate(date);
		debit.setDescription(account.getName() + " Bank Account debit side");
		debit.setPublicId(helperServices.getGenPublicId());
		debit.setUser(authenticationServices.getCurrentUser());

		return debit;
	}

	@Override
	public BankTransection initBankTransaction(BankDeposit deposit) {

		if (deposit != null) {
			Date date = new Date();
			BankTransection bankTransection = new BankTransection();
			bankTransection.setDeposit(deposit);
			bankTransection.setAmount(deposit.getAmount());
			bankTransection.setDate(date);
			bankTransection.setDateGroup(date);
			bankTransection.setReceive(1);
			bankTransection.setPublicId(helperServices.getGenPublicId());

			return bankTransection;
		}
		return null;
	}

	@Override
	public BankTransection initBankTransactionViaWithdraw(BankWithdraw withdraw, Date date) {

		if (withdraw != null) {
			BankTransection bankTransection = new BankTransection();
			bankTransection.setAmount(withdraw.getAmount());
			bankTransection.setBankWithdraw(withdraw);
			bankTransection.setDate(date);
			bankTransection.setDateGroup(date);
			bankTransection.setPay(1);
			bankTransection.setPublicId(helperServices.getGenPublicId());

			return bankTransection;
		}
		return null;
	}

	@Override
	public BankAccountCreditRecord initBankCreditRecord(BankAccountCredit dbSourceCredit, Date date) {

		if (dbSourceCredit != null) {
			BankAccountCreditRecord accountCreditRecord = new BankAccountCreditRecord();
			accountCreditRecord.setAccountCredit(dbSourceCredit);
			accountCreditRecord.setDate(date);
			accountCreditRecord.setDateGroup(date);
			accountCreditRecord.setPublicId(helperServices.getGenPublicId());
			accountCreditRecord.setUser(authenticationServices.getCurrentUser());

			return accountCreditRecord;
		}
		return null;
	}

	@Override
	public BankAccountDebitRecord initBankDebitRecord(BankAccountDebit dbDestinationDebit, Date date) {

		if (dbDestinationDebit != null) {

			BankAccountDebitRecord accountDebitRecord = new BankAccountDebitRecord();
			accountDebitRecord.setAccountDebit(dbDestinationDebit);
			accountDebitRecord.setDate(date);
			accountDebitRecord.setDateGroup(date);
			accountDebitRecord.setPublicId(helperServices.getGenPublicId());
			accountDebitRecord.setUser(authenticationServices.getCurrentUser());

			return accountDebitRecord;
		}

		return null;
	}

	@Override
	public Stakeholder initStakeholderViaUser(User dbUser, Date date) {

		Stakeholder stakeholder = new Stakeholder();
		stakeholder.setActive(true);
		stakeholder.setApprove(1);
		stakeholder.setEmail(dbUser.getEmail());
		stakeholder.setName(dbUser.getName());
		stakeholder.setPhoneNo(dbUser.getPhoneNo());
		stakeholder.setUser(dbUser);
		stakeholder.setPublicId(helperServices.getGenPublicId());
		stakeholder.setGenId(helperServices.getUserGenId());
		return stakeholder;
	}
	
	@Override
	public Cart initCart(Date date) {

		Cart cart = new Cart();
		cart.setDate(date);
		cart.setPublicId(helperServices.getGenPublicId());
		cart.setUpdateDate(date);
		return cart;
	}

}
