package com.altqart.mapper;

import java.util.List;

import com.altqart.model.InvoiceSetting;
import com.altqart.model.NamePhoneNo;
import com.altqart.model.Store;
import com.altqart.model.StoreType;
import com.altqart.req.model.NamePhoneNoReq;
import com.altqart.req.model.StoreReq;
import com.altqart.resp.model.RespInvoiceSetting;
import com.altqart.resp.model.RespNamePhoneNo;
import com.altqart.resp.model.RespStore;
import com.altqart.resp.model.RespStoreType;

public interface StoreMapper {

	public RespStore getRespStoreInf(Store store);

	public List<RespNamePhoneNo> mapAllRespNamePhoneNo(List<NamePhoneNo> namePhoneNos);

	public RespStoreType mapStoreType(StoreType storeType);

	public RespNamePhoneNo mapRespNamePhoneNo(NamePhoneNo namePhoneNo);

	public Store mapStore(StoreReq storeReq);

	public RespStore mapRespStore(Store store);

	public RespStore mapRespStoreOnly(Store store);

	public List<RespStore> mapAllRespStore(List<Store> stores);

	public NamePhoneNo mapNameAndPhoneNo(NamePhoneNoReq namePhoneNo);

	public List<RespStoreType> mapAllRespStoreType(List<StoreType> storeTypes);

	public RespStoreType mapRespStoreType(StoreType storeType);

	public RespStoreType mapRespStoreTypeOnly(StoreType storeType);

	public List<RespInvoiceSetting> mapAllInvoiceSetting(List<InvoiceSetting> invoiceSettings);

	public RespInvoiceSetting getSettingByValue(String value, List<InvoiceSetting> invoiceSettings);

}
