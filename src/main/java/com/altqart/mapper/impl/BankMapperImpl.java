package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.BankMapper;
import com.altqart.model.Bank;
import com.altqart.model.BankType;
import com.altqart.req.model.BankReq;
import com.altqart.resp.model.RespBank;
import com.altqart.resp.model.RespBankType;

@Service
public class BankMapperImpl implements BankMapper {

	@Autowired
	private HelperServices helperServices;

	@Override
	public Bank mapBank(BankReq bankReq) {

		if (bankReq != null) {

			Bank bank = new Bank();
			bank.setDescription(bankReq.getDescription());
			bank.setName(bankReq.getName());
			bank.setPublicId(helperServices.getGenPublicId());
			bank.setLogoUrl(bankReq.getLogoUrl());

			return bank;
		}

		return null;
	}

	@Override
	public List<RespBank> mapAllRespBank(List<Bank> banks) {

		if (banks != null) {

			List<RespBank> listBanks = new ArrayList<>();
			for (Bank bank : banks) {

				RespBank respBank = mapRespBank(bank);

				if (respBank != null) {
					listBanks.add(respBank);
				}
			}

			return listBanks;
		}

		return null;
	}

	@Override
	public RespBank mapRespBank(Bank bank) {

		if (bank != null) {

			RespBank respBank = new RespBank();

			respBank.setDescription(bank.getDescription());
			respBank.setId(bank.getPublicId());
			respBank.setName(bank.getName());
			respBank.setLogoUrl(bank.getLogoUrl());
			return respBank;
		}

		return null;
	}

	@Override
	public RespBank mapBankOnly(Bank bank) {

		if (bank != null) {
			RespBank respBank = new RespBank();

			respBank.setDescription(bank.getDescription());
			respBank.setId(bank.getPublicId());
			respBank.setName(bank.getName());
			respBank.setLogoUrl(bank.getLogoUrl());

			return respBank;
		}

		return null;
	}

	@Override
	public List<RespBankType> mapAllRespBankType(List<BankType> bankTypes) {

		if (bankTypes != null) {

			List<RespBankType> respBankTypes = new ArrayList<>();

			for (BankType bankType : bankTypes) {
				RespBankType type = mapRespBankTypeOnly(bankType);

				if (type != null) {
					respBankTypes.add(type);
				}
			}
			
			return respBankTypes;
		}

		return null;
	}

	private RespBankType mapRespBankTypeOnly(BankType bankType) {

		if (bankType != null) {

			RespBankType type = new RespBankType();
			type.setDescription(bankType.getDescription());
			type.setId(bankType.getId());
			type.setKey(bankType.getKey());
			type.setName(bankType.getName());
			return type;
		}
		return null;
	}

}
