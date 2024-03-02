package com.altqart.mapper;

import java.util.List;
import java.util.Map;

import com.altqart.model.SaleReturnInvoice;
import com.altqart.model.SaleReturnItem;
import com.altqart.req.model.SaleReturnItemReq;
import com.altqart.req.model.SaleReturnReq;
import com.altqart.resp.model.RespSaleReturn;
import com.altqart.resp.model.RespSaleReturnItem;

public interface SaleReturnInvoiceMapper {

	public List<RespSaleReturn> mapAllSaleReturnInvoice(List<SaleReturnInvoice> saleReturnInvoice);

	public RespSaleReturn mapSaleReturnInvoice(SaleReturnInvoice saleReturnInvoice);

	public SaleReturnInvoice mapSaleReturnInvoice(SaleReturnReq saleReturnReq, Map<String, Object> map) throws Exception;
	
	public RespSaleReturn mapSaleReturnInvoiceOnly(SaleReturnInvoice returnInvoice);
	
	public List<RespSaleReturnItem> mapAllSaleReturnItem(List<SaleReturnItem> returnItems);
	
	public RespSaleReturnItem mapSaleReturnItemOnly(SaleReturnItem saleReturnItem);
	
	
	public SaleReturnItem mapSaleReturnItem(SaleReturnItemReq returnItemReq) throws Exception;
}
