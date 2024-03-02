package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.BankAccountMapper;
import com.altqart.mapper.MethodAndTransactionMapper;
import com.altqart.model.MethodAndTransaction;
import com.altqart.req.model.MethodAndTransactionReq;
import com.altqart.resp.model.RespMethodAndTransaction;
import com.altqart.services.BankAccountServices;

@Service
public class MethodAndTransactionMapperImp implements MethodAndTransactionMapper {

	@Autowired
	private BankAccountServices bankAccountServices;

	@Autowired
	private BankAccountMapper bankAccountMapper;
	
	@Autowired
	private HelperServices helperServices;

	@Override
	public List<MethodAndTransaction> mapAllMethodAndTransaction(List<MethodAndTransactionReq> methodAndTransactions) {

		if (methodAndTransactions != null) {
			List<MethodAndTransaction> transactions = new ArrayList<>();

			for (MethodAndTransactionReq methodAndTransactionReq : methodAndTransactions) {

				MethodAndTransaction methodAndTransaction = mapMethodAndTransaction(methodAndTransactionReq);

				if (methodAndTransaction != null) {
					transactions.add(methodAndTransaction);
				}
			}

			return transactions;
		}
		return null;
	}

	@Override
	public MethodAndTransaction mapMethodAndTransaction(MethodAndTransactionReq methodAndTransaction) {

		if (methodAndTransaction != null) {
			MethodAndTransaction transaction = new MethodAndTransaction();
			transaction.setSource(bankAccountServices.getBanksAccountById(methodAndTransaction.getSource()));
			transaction.setDestination(bankAccountServices.getBanksAccountById(methodAndTransaction.getDestination()));
			transaction.setAmount(methodAndTransaction.getAmount());
			transaction.setRefNo(methodAndTransaction.getRefNo());
			transaction.setPublicId(helperServices.getGenPublicId());
			return transaction;
		}

		return null;
	}

	@Override
	public List<RespMethodAndTransaction> mapAllRespMethodAndTransaction(
			List<MethodAndTransaction> methodAndTransactions) {
		if (methodAndTransactions != null) {
			List<RespMethodAndTransaction> respMethodAndTransactions = new ArrayList<>();

			for (MethodAndTransaction methodAndTransaction : methodAndTransactions) {
				RespMethodAndTransaction transaction = mapMethodAndTransaction(methodAndTransaction);

				if (transaction != null) {
					respMethodAndTransactions.add(transaction);
				}
			}

			return respMethodAndTransactions;
		}
		return null;
	}

	@Override
	public RespMethodAndTransaction mapMethodAndTransaction(MethodAndTransaction methodAndTransaction) {

		if (methodAndTransaction != null) {
			RespMethodAndTransaction respMethodAndTransaction = mapMethodAndTransactionOnly(methodAndTransaction);

			if (methodAndTransaction.getSource() != null) {
				respMethodAndTransaction
						.setSource(bankAccountMapper.mapRespBankAccountOnly(methodAndTransaction.getSource()));
			}

			if (methodAndTransaction.getDestination() != null) {
				respMethodAndTransaction.setDestination(
						bankAccountMapper.mapRespBankAccountOnly(methodAndTransaction.getDestination()));
			}

			return respMethodAndTransaction;
		}

		return null;
	}

	@Override
	public RespMethodAndTransaction mapMethodAndTransactionOnly(MethodAndTransaction methodAndTransaction) {

		if (methodAndTransaction != null) {
			RespMethodAndTransaction transaction = new RespMethodAndTransaction();
			transaction.setAmount(methodAndTransaction.getAmount());
			transaction.setId(methodAndTransaction.getPublicId());
			transaction.setRefNo(methodAndTransaction.getRefNo());

			return transaction;
		}

		return null;
	}

}
