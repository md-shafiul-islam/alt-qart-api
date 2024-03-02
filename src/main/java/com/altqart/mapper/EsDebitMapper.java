package com.altqart.mapper;

import java.util.List;

import com.altqart.model.EsDebit;
import com.altqart.model.EsDebitDetail;
import com.altqart.resp.model.RespEsDebit;
import com.altqart.resp.model.RespEsDebitDetail;

public interface EsDebitMapper {

	public RespEsDebit mapRespEsDebit(EsDebit debit);

	public RespEsDebit mapRespEsDebitWithDetails(EsDebit debit);
	
	public List<RespEsDebitDetail> mapAllDebitDetals(List<EsDebitDetail> debitDetails);
	
	public RespEsDebitDetail mapDebitDebitDetails(EsDebitDetail esDebitDetail);
	


}
