package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Address;
import com.altqart.model.ShippingAddress;
import com.altqart.req.model.AddressReq;
import com.altqart.resp.model.RespAddress;
import com.altqart.resp.model.RespMinAddress;
import com.altqart.resp.model.RespShippingAddress;

public interface AddressMapper {

	public Address mapAddress(AddressReq addressReq);

	public List<RespAddress> mapAllRespAddress(List<Address> addresses);

	public List<RespMinAddress> mapAllRespAddressMin(List<Address> addresses);

	public RespShippingAddress mapRespShippingAddressOnly(ShippingAddress shippingAddress);

}
