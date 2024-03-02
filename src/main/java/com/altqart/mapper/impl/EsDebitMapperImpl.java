package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.mapper.EsDebitMapper;
import com.altqart.mapper.UserMapper;
import com.altqart.model.EsDebit;
import com.altqart.model.EsDebitDetail;
import com.altqart.resp.model.RespEsDebit;
import com.altqart.resp.model.RespEsDebitDetail;

@Service
public class EsDebitMapperImpl implements EsDebitMapper {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public RespEsDebit mapRespEsDebit(EsDebit debit) {

		if (debit != null) {

			RespEsDebit esDebit = new RespEsDebit();

			esDebit.setAmount(debit.getAmount());
			esDebit.setId(debit.getPublicId());

			return esDebit;
		}
		return null;
	}
	
	@Override
	public RespEsDebit mapRespEsDebitWithDetails(EsDebit debit) {
		
		if(debit != null) {
			RespEsDebit esDebit = mapRespEsDebit(debit);
			
			if(debit.getDebitDetails() != null) {
				List<RespEsDebitDetail>debitDetails = mapAllDebitDetals(debit.getDebitDetails());
				esDebit.setDebitDetails(debitDetails);
			}
			
			
			return esDebit;
		}
		
		return null;
	}

	@Override
	public List<RespEsDebitDetail> mapAllDebitDetals(List<EsDebitDetail> debitDetails) {
		
		if(debitDetails != null) {
			List<RespEsDebitDetail> details = new ArrayList<>();
			for (EsDebitDetail esDebitDetail : debitDetails) {
				RespEsDebitDetail debitDetail = mapDebitDebitDetails(esDebitDetail);
				if(debitDetail != null) {
					details.add(debitDetail);
				}
			}
			return details;
		}
		
		return null;
	}

	@Override
	public RespEsDebitDetail mapDebitDebitDetails(EsDebitDetail esDebitDetail) {
		if(esDebitDetail != null) {
			
			RespEsDebitDetail debitDetail = new RespEsDebitDetail();
			debitDetail.setAmount(esDebitDetail.getAmount());
			debitDetail.setDate(esDebitDetail.getDate());
			debitDetail.setDateGroup(esDebitDetail.getDateGroup());
			debitDetail.setNote(esDebitDetail.getNote());
			debitDetail.setId(esDebitDetail.getId());
			if(esDebitDetail.getUser() != null) {
				debitDetail.setUser(userMapper.mapRespEsUser(esDebitDetail.getUser()));
			}
			debitDetail.setStatus(esDebitDetail.getStatus());
			return debitDetail;
			
		}
		return null;
	}
	
	

}
