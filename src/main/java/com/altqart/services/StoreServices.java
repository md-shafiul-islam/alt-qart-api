package com.altqart.services;

import java.util.List;
import java.util.Map;

import com.altqart.model.Store;
import com.altqart.req.model.BankAccountReq;
import com.altqart.req.model.EsApprove;
import com.altqart.req.model.NamePhoneNoReq;
import com.altqart.req.model.StoreReq;
import com.altqart.resp.model.RespNamePhoneNo;
import com.altqart.resp.model.RespStore;
import com.altqart.resp.model.RespStoreType;

public interface StoreServices {

	public Store getStoreById(int id);

	public void updateStore(Store store);

	public void addStore(Store store);

	public Store getStoreById(String id);

	public Store getDefaultStore();

	public void addStore(StoreReq storeReq, Map<String, Object> map);

	public void addApprove(EsApprove approve, Map<String, Object> map);

	public RespStore getRespStoreById(String id);

	public List<RespStore> getAllStore(int i, int start, int size);

	public void update(StoreReq StoreReq, Map<String, Object> map);

	public boolean updateApprove(EsApprove approve);

	public void addOrUpdaPhoneNoAll(List<NamePhoneNoReq> namePhoneNoReqs, String store, Map<String, Object> map);

	public void addBankAccount(BankAccountReq bankAccountReq, Map<String, Object> map);

	public void getAllBankAccountByStore(String id, Map<String, Object> map);

	public void getAllStoreNamePhoneNo(String id, Map<String, Object> map);

	public void addStoreNamePhoneNo(String id, NamePhoneNoReq namePhoneNo, Map<String, Object> map);

	public void updateStoreNamePhoneNo(String id, NamePhoneNoReq namePhoneNo, Map<String, Object> map);

	public RespNamePhoneNo getStoreByPhoneId(int phoneId);

	public void removeStoreNamePhone(String id, NamePhoneNoReq namePhoneNo, Map<String, Object> map);

	public List<RespStoreType> getAllRespStoreType();


}
