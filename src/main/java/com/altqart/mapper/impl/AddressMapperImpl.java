package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.AddressMapper;
import com.altqart.model.Address;
import com.altqart.model.Area;
import com.altqart.model.City;
import com.altqart.model.Zone;
import com.altqart.req.model.AddressReq;
import com.altqart.resp.model.RespAddress;
import com.altqart.services.AreasServices;
import com.altqart.services.CitiyServices;
import com.altqart.services.ZoneServices;

@Service
public class AddressMapperImpl implements AddressMapper {

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private CitiyServices citiyServices;

	@Autowired
	private ZoneServices zoneServices;

	@Autowired
	private AreasServices areasServices;

	@Override
	public Address mapAddress(AddressReq addressReq) {

		if (addressReq != null) {

			Address address = new Address();

			City city = citiyServices.getCityByKey(addressReq.getCity());
			Zone zone = zoneServices.getZoneByKey(addressReq.getZone());
			Area area = areasServices.getAreaByKey(addressReq.getArea());

			if (area == null || zone == null || area == null) {
				return null;
			}

			address.setFullName(addressReq.getFullName());
			address.setPublicId(helperServices.getGenPublicId());
			address.setArea(area);
			address.setZone(zone);
			address.setCity(city);

			return address;
		}

		return null;
	}

	@Override
	public List<RespAddress> mapAllRespAddress(List<Address> addresses) {

		if (addresses != null) {

			List<RespAddress> respAddresses = new ArrayList<>();

			for (Address address : addresses) {

				RespAddress respAddress = mapRespAddress(address);

				if (respAddress != null) {
					respAddresses.add(respAddress);
				}
			}

			return respAddresses;
		}
		return null;
	}

	public RespAddress mapRespAddress(Address address) {

		if (address != null) {

			RespAddress respAddress = new RespAddress();
			respAddress.setId(address.getPublicId());
			respAddress.setFullName(address.getFullName());
			if (address.getArea() != null) {
				respAddress.setArea(address.getArea().getName());
			}

			if (address.getCity() != null) {
				respAddress.setCity(address.getCity().getName());
			}

			if (address.getZone() != null) {
				respAddress.setZone(address.getZone().getName());
			}

			respAddress.setDefault(address.isDefault());
			respAddress.setOffice(address.isOffice());
			respAddress.setZipCode(address.getZipCode());

			return respAddress;
		}

		return null;
	}
}
