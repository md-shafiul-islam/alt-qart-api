package com.altqart.services;

import java.util.List;
import java.util.Map;

import com.altqart.model.SaleReturnInvoice;
import com.altqart.req.model.EsApprove;
import com.altqart.req.model.SaleReturnReq;
import com.altqart.resp.model.RespSaleReturn;

public interface SaleReturnServices {

	public List<RespSaleReturn> getAllSaleRetun(int start, int size);

	public SaleReturnInvoice getSaleRetunById(String id);

	public void addSaleReturnInvoice(SaleReturnReq saleReturnReq, Map<String, Object> map);

	public void addApproveSaleReturnInvoice(EsApprove approve, Map<String, Object> map);
	
	public RespSaleReturn getRespSaleRetunById(String id);

	public void addRejectSaleReturnInvoice(EsApprove approve, Map<String, Object> map);

	public List<RespSaleReturn> getAllPendingSaleRetun(int start, int size);
	
	public List<RespSaleReturn> getAllRejectedSaleRetun(int start, int size);

}