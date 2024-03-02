package com.altqart.initializer.services;

import java.util.Date;

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

public interface EsInitializerServices {

	public EsCreditDetail initEsCreditDetail(EsCredit esCredit, Date date);

	public EsDebitDetail initEsDebitDetail(EsDebit esDebit, Date date);

	public EsCredit initEsCreditByStakeholder(Stakeholder stakeholder, Date date);

	public EsDebit initEsDebitByStakeholder(Stakeholder stakeholder, Date date);

	public EsDebit initEsDebitBySupplier(Stakeholder stakeholder, Date date);

	public EsCredit initEsCreditBySupplier(Stakeholder stakeholder, Date date);

	public BankTransection initBankTransaction(BankDeposit bankDeposit);

	public BankTransection initBankTransactionViaWithdraw(BankWithdraw dbWithdraw, Date date);

	public BankAccountCredit initBankAccountCredit(BankAccount bankAccount, Date date);

	public BankAccountDebit initBankAccountDebit(BankAccount bankAccount, Date date);

	public BankAccountCreditRecord initBankCreditRecord(BankAccountCredit dbSourceCredit, Date date);

	public BankAccountDebitRecord initBankDebitRecord(BankAccountDebit dbDestinationDebit, Date date);

	public Stakeholder initStakeholderViaUser(User dbUser, Date date);

	public Cart initCart(Date date);
	
}
