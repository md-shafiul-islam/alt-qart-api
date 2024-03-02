package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.MethodAndTransactionMapper;
import com.altqart.mapper.ReceiveMapper;
import com.altqart.mapper.StakeholderMapper;
import com.altqart.mapper.UserMapper;
import com.altqart.model.Receive;
import com.altqart.resp.model.RespReceive;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReceiveMapperImpl implements ReceiveMapper {

	@Autowired
	private StakeholderMapper stakeholderMapper;

	@Autowired
	private MethodAndTransactionMapper methodAndTransactionMapper;

	@Autowired
	private UserMapper userMapper;

	@Override
	public List<RespReceive> mapAllRespReceive(List<Receive> receives) {

		if (receives != null) {
			List<RespReceive> respReceives = new ArrayList<>();
			for (Receive receive : receives) {

				RespReceive respReceive = mapRespReceive(receive);

				if (respReceive != null) {
					respReceives.add(respReceive);
				}
			}

			return respReceives;
		}

		return null;
	}

	@Override
	public RespReceive mapRespReceive(Receive receive) {

		if (receive != null) {

			RespReceive respReceive = mapReceiveOnly(receive);

			respReceive.setEsStakeholder(stakeholderMapper.mapRepEsStakeholder(receive.getStakeholder()));

			return respReceive;
		}

		return null;
	}

	private RespReceive mapReceiveOnly(Receive receive) {
		RespReceive respReceive = new RespReceive();
		
		respReceive.setInvId(receive.getReceiveInv());
		respReceive.setAmount(receive.getAmount());
		respReceive.setApprove(receive.getApprove());
		respReceive.setDate(receive.getDate());
		respReceive.setDateGroup(receive.getDateGroup());
		respReceive.setDescription(receive.getDescription());

		respReceive.setId(receive.getPublicId());
		respReceive.setMethodAndTransactions(
				methodAndTransactionMapper.mapAllRespMethodAndTransaction(receive.getMethodAndTransactions()));
		respReceive.setUser(userMapper.mapRespEsUser(receive.getUser()));
		respReceive.setDiscount(receive.getDiscount());
		respReceive.setPresentDebit(receive.getPresentDebit());
		respReceive.setPrevDebit(receive.getPrevDebit());
		respReceive.setGrandTotal(receive.getGrandTotal());
		return respReceive;
	}

}
