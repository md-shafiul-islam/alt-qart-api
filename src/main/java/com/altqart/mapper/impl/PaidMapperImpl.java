package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.MethodAndTransactionMapper;
import com.altqart.mapper.PaidMapper;
import com.altqart.mapper.StakeholderMapper;
import com.altqart.mapper.UserMapper;
import com.altqart.model.Paid;
import com.altqart.model.Receive;
import com.altqart.req.model.PaidReq;
import com.altqart.req.model.ReceiveReq;
import com.altqart.resp.model.RespPaid;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaidMapperImpl implements PaidMapper {

	@Autowired
	private StakeholderMapper stakeholderMapper;

	@Autowired
	private MethodAndTransactionMapper methodAndTransactionMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private HelperServices helperServices;

	@Override
	public List<RespPaid> mapAllRespPaid(List<Paid> paids) {

		if (paids != null) {
			List<RespPaid> payeds = new ArrayList<>();
			for (Paid paid : paids) {

				RespPaid RespPaid = mapRespPaid(paid);
				if (RespPaid != null) {
					payeds.add(RespPaid);
				}
			}

			return payeds;
		}

		return null;
	}

	@Override
	public RespPaid mapRespPaid(Paid paid) {

		if (paid != null) {

			RespPaid RespPaid = mapPaidOnly(paid);

			RespPaid.setEsStakeholder(stakeholderMapper.mapRepEsStakeholder(paid.getStakeholder()));

			return RespPaid;
		}

		return null;
	}

	private RespPaid mapPaidOnly(Paid paid) {
		RespPaid RespPaid = new RespPaid();

		RespPaid.setAmount(paid.getAmount());
		RespPaid.setApprove(paid.getApprove());
		RespPaid.setDateGroup(paid.getDateGroup());
		RespPaid.setDescription(paid.getDescription());

		RespPaid.setId(paid.getPublicId());
		RespPaid.setUser(userMapper.mapRespEsUser(paid.getUser()));
		RespPaid.setDiscount(paid.getDiscount());
		RespPaid.setPresentCredit(paid.getPresentCredit());
		RespPaid.setPrevCredit(paid.getPrevCredit());

		RespPaid.setMethodAndTransactions(
				methodAndTransactionMapper.mapAllRespMethodAndTransaction(paid.getMethodAndTransactions()));

		RespPaid.setInvId(paid.getInvId());
		RespPaid.setGrandTotal(paid.getGrandTotal());

		return RespPaid;
	}

	@Override
	public Paid mapPayed(PaidReq payed) {

		if (payed != null) {
			Paid nPayed = new Paid();
			nPayed.setAmount(payed.getAmount());
			nPayed.setDescription(payed.getDescription());

			nPayed.setDiscount(payed.getDiscount());
			nPayed.setGrandTotal(payed.getGrandTotal());
			nPayed.setPublicId(helperServices.getGenPublicId());
			nPayed.setInvId(helperServices.getGenaretedReceiptIDUsingDateTime());

			if (payed.getTransactionMethods() != null) {
				nPayed.setMethodAndTransactions(
						methodAndTransactionMapper.mapAllMethodAndTransaction(payed.getTransactionMethods()));
			}
			return nPayed;
		}
		return null;
	}

	@Override
	public Receive mapReceive(ReceiveReq receive) {

		Receive nReceive = new Receive();
		nReceive.setPublicId(helperServices.getGenPublicId());

		return nReceive;
	}

	@Override
	public List<RespPaid> mapAllRespPayed(List<Paid> paids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RespPaid mapRespPayed(Paid paid) {
		// TODO Auto-generated method stub
		return null;
	}

}
