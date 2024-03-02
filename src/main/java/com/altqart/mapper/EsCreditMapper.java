package com.altqart.mapper;

import java.util.List;

import com.altqart.model.EsCredit;
import com.altqart.model.EsCreditDetail;
import com.altqart.resp.model.RespEsCredit;
import com.altqart.resp.model.RespEsCreditDetail;

public interface EsCreditMapper {

	public RespEsCredit mapRespEsCredit(EsCredit credit);

	public RespEsCredit mapRespEsCreditWithDetails(EsCredit credit);
	
	public RespEsCreditDetail mapCreditDetails(EsCreditDetail esCreditDetail);
	

	public List<RespEsCreditDetail> mapAllCreditDetals(List<EsCreditDetail> creditDetails);

}
