package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Address;
import com.altqart.req.model.AddressReq;
import com.altqart.resp.model.RespAddress;

public interface AddressMapper {

	public Address mapAddress(AddressReq addressReq);

	public List<RespAddress> mapAllRespAddress(List<Address> addresses);

}
