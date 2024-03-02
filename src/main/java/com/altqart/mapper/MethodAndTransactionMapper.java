package com.altqart.mapper;

import java.util.List;

import com.altqart.model.MethodAndTransaction;
import com.altqart.req.model.MethodAndTransactionReq;
import com.altqart.resp.model.RespMethodAndTransaction;

public interface MethodAndTransactionMapper {

	public List<MethodAndTransaction> mapAllMethodAndTransaction(List<MethodAndTransactionReq> methodAndTransactions);
	
	public MethodAndTransaction mapMethodAndTransaction(MethodAndTransactionReq methodAndTransaction);
	
	public RespMethodAndTransaction mapMethodAndTransactionOnly(MethodAndTransaction methodAndTransaction);
	
	public RespMethodAndTransaction mapMethodAndTransaction(MethodAndTransaction methodAndTransaction);
	
	public List<RespMethodAndTransaction> mapAllRespMethodAndTransaction(List<MethodAndTransaction> methodAndTransaction);

}
