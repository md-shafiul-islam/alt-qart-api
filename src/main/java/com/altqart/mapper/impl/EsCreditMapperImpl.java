package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.EsCreditMapper;
import com.altqart.mapper.UserMapper;
import com.altqart.model.EsCredit;
import com.altqart.model.EsCreditDetail;
import com.altqart.resp.model.RespEsCredit;
import com.altqart.resp.model.RespEsCreditDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EsCreditMapperImpl implements EsCreditMapper {

	@Autowired
	private UserMapper userMapper;

	@Override
	public RespEsCredit mapRespEsCredit(EsCredit credit) {

		if (credit != null) {

			RespEsCredit esCredit = new RespEsCredit();

			esCredit.setAmount(credit.getAmount());
			esCredit.setId(credit.getPublicId());
			return esCredit;
		}

		return null;
	}

	@Override
	public RespEsCredit mapRespEsCreditWithDetails(EsCredit credit) {

		if (credit != null) {
			RespEsCredit esCredit = mapRespEsCredit(credit);

			if (credit.getCreditDetails() != null) {
				List<RespEsCreditDetail> creditDetails = mapAllCreditDetals(credit.getCreditDetails());
				esCredit.setCreditDetails(creditDetails);
			}

			return esCredit;
		}

		return null;
	}

	@Override
	public List<RespEsCreditDetail> mapAllCreditDetals(List<EsCreditDetail> creditDetails) {

		if (creditDetails != null) {
			
			List<RespEsCreditDetail> details = new ArrayList<>();
			for (EsCreditDetail esCreditDetail : creditDetails) {
				RespEsCreditDetail creditDetail = mapCreditDetails(esCreditDetail);
				if (creditDetail != null) {
					details.add(creditDetail);
				}
			}
			return details;
		}

		return null;
	}

	@Override
	public RespEsCreditDetail mapCreditDetails(EsCreditDetail esCreditDetail) {
		if (esCreditDetail != null) {

			RespEsCreditDetail creditDetail = new RespEsCreditDetail();
			
			creditDetail.setAmount(esCreditDetail.getAmount());
			
			creditDetail.setDate(esCreditDetail.getDate());
			creditDetail.setDateGroup(esCreditDetail.getDateGroup());
			creditDetail.setNote(esCreditDetail.getNote());
			creditDetail.setId(esCreditDetail.getId());
			if (esCreditDetail.getUser() != null) {
				creditDetail.setUser(userMapper.mapRespEsUser(esCreditDetail.getUser()));
			}
			creditDetail.setStatus(esCreditDetail.getStatus());
			
			return creditDetail;

		}
		return null;
	}

}
