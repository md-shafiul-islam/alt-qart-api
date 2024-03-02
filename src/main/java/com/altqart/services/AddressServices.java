package com.altqart.services;

import java.util.List;
import java.util.Map;

import com.altqart.req.model.AddressReq;
import com.altqart.resp.model.SearchWordReq;

public interface AddressServices {

	public void getAll(Map<String, Object> map, int start, int size);

	public void add(AddressReq addressReq, Map<String, Object> map);

	public void remove(AddressReq addressReq, Map<String, Object> map);

	public void update(AddressReq addressReq, Map<String, Object> map);

	public void getAllSearchSuggestion(SearchWordReq words, Map<String, Object> map);

	public void getStakeholderAddress(Map<String, Object> map);

}
