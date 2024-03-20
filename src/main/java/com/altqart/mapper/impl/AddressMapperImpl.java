package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.AddressMapper;
import com.altqart.mapper.AreaMapper;
import com.altqart.mapper.CityMapper;
import com.altqart.mapper.ZoneMapper;
import com.altqart.model.Address;
import com.altqart.model.Area;
import com.altqart.model.City;
import com.altqart.model.ShippingAddress;
import com.altqart.model.Zone;
import com.altqart.req.model.AddressReq;
import com.altqart.resp.model.RespAddress;
import com.altqart.resp.model.RespMinAddress;
import com.altqart.resp.model.RespNameCode;
import com.altqart.resp.model.RespShippingAddress;
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

	@Autowired
	private AreaMapper areaMapper;

	@Autowired
	private ZoneMapper zoneMapper;

	@Autowired
	private CityMapper cityMapper;

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
			
			address.setPhoneNo(addressReq.getPhoneNo());
			address.setPhoneNo2(addressReq.getPhoneNoOpt());
			address.setZipCode(addressReq.getZipCode());
			
			if(!helperServices.isNullOrEmpty(addressReq.getFullAddress())) {
				address.setFullAddress(addressReq.getFullAddress());
			}
			
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
				respAddress.setArea(areaMapper.mapRespAreaOnly(address.getArea()));
			}

			if (address.getCity() != null) {
				respAddress.setCity(cityMapper.mapRespCityOnly(address.getCity()));
			}

			if (address.getZone() != null) {
				respAddress.setZone(zoneMapper.mapRespZoneOnly(address.getZone()));
			}

			respAddress.setDefault(address.isDefault());
			respAddress.setOffice(address.isOffice());
			respAddress.setZipCode(address.getZipCode());

			return respAddress;
		}

		return null;
	}

	@Override
	public List<RespMinAddress> mapAllRespAddressMin(List<Address> addresses) {

		if (addresses != null) {

			List<RespMinAddress> respAddresses = new ArrayList<>();

			for (Address address : addresses) {

				RespMinAddress respAddress = mapRespAddressMin(address);

				if (respAddress != null) {
					respAddresses.add(respAddress);
				}
			}

			return respAddresses;
		}
		return null;
	}

	private RespMinAddress mapRespAddressMin(Address address) {

		if (address != null) {

			RespMinAddress respAddress = new RespMinAddress();
			respAddress.setId(address.getPublicId());
			respAddress.setFullName(address.getFullName());
			if (address.getArea() != null) {
				respAddress.setArea(areaMapper.mapNameCode(address.getArea()));
			}

			if (address.getCity() != null) {
				respAddress.setCity(cityMapper.mapNameCode(address.getCity()));
			}

			if (address.getZone() != null) {
				respAddress.setZone(zoneMapper.mapNameCode(address.getZone()));
			}

			respAddress.setDefault(address.isDefault());
			respAddress.setOffice(address.isOffice());
			respAddress.setZipCode(address.getZipCode());

			return respAddress;
		}
		return null;
	}

	@Override
	public RespShippingAddress mapRespShippingAddressOnly(ShippingAddress shippingAddress) {
		RespShippingAddress address = new RespShippingAddress();
		if (shippingAddress != null) {

			if (shippingAddress.getAddress() != null) {

				if (shippingAddress.getAddress().getArea() != null) {
					address.setArea(shippingAddress.getAddress().getArea().getName());
				}

				if (shippingAddress.getAddress().getCity() != null) {
					address.setCity(shippingAddress.getAddress().getCity().getName());
				}

				if (shippingAddress.getAddress().getZone() != null) {
					address.setZone(shippingAddress.getAddress().getZone().getName());
				}

				address.setFullName(shippingAddress.getAddress().getFullName());
				address.setPhoneNo(shippingAddress.getAddress().getPhoneNo());
				address.setPhoneNo2(shippingAddress.getAddress().getPhoneNo2());
			}
		}

		return address;
	}

}
