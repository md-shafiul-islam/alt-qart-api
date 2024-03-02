package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.altqart.mapper.BankAccountTypeMapper;
import com.altqart.model.BankAccountType;
import com.altqart.resp.model.RespBankAccountType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BankAccountTypeMapperImpl implements BankAccountTypeMapper {

	@Override
	public List<RespBankAccountType> mapAllRespBankAccountType(List<BankAccountType> accountTypes) {

		if (accountTypes != null) {
			List<RespBankAccountType> bankAccountTypes = new ArrayList<>();

			for (BankAccountType accountType : accountTypes) {
				RespBankAccountType bankAccountType = mapRespBankAccountTypeOnly(accountType);

				if (bankAccountType != null) {
					bankAccountTypes.add(bankAccountType);
				}
			}

			return bankAccountTypes;
		}

		return null;
	}

	@Override
	public RespBankAccountType mapRespBankAccountTypeOnly(BankAccountType accountType) {

		if (accountType != null) {
			RespBankAccountType bankAccountType = new RespBankAccountType();
			bankAccountType.setDescription(accountType.getDescription());
			bankAccountType.setValue(accountType.getValue());
			bankAccountType.setName(accountType.getName());
			bankAccountType.setId(accountType.getId());
			return bankAccountType;
		}

		return null;
	}

}
