package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.StoreMapper;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StoreMapperImpl implements StoreMapper {

	@Autowired
	private HelperServices helperServices;

	@Override
	public RespStore mapRespStore(Store store) {

		if (store != null) {

			RespStore respStore = mapRespStoreOnly(store);

			if (respStore != null) {
				respStore.setNamePhoneNos(mapAllRespNamePhoneNo(store.getNamePhoneNos()));

			}

			return respStore;
		}

		return null;
	}

	@Override
	public RespStore getRespStoreInf(Store store) {
		if (store != null) {

			RespStore respStore = mapRespStoreOnly(store);

			if (respStore != null) {
				respStore.setNamePhoneNos(mapAllRespNamePhoneNo(store.getNamePhoneNos()));
			}

			return respStore;
		}

		return null;
	}

	@Override
	public List<RespNamePhoneNo> mapAllRespNamePhoneNo(List<NamePhoneNo> namePhoneNos) {

		if (namePhoneNos != null) {

			List<RespNamePhoneNo> phoneNos = new ArrayList<>();

			for (NamePhoneNo namePhoneNo : namePhoneNos) {

				RespNamePhoneNo phoneNo = mapRespNamePhoneNo(namePhoneNo);

				if (phoneNo != null) {
					phoneNos.add(phoneNo);
				}
			}

			return phoneNos;
		}
		return null;
	}

	@Override
	public RespNamePhoneNo mapRespNamePhoneNo(NamePhoneNo namePhoneNo) {

		RespNamePhoneNo phoneNo = new RespNamePhoneNo();
		phoneNo.setId(namePhoneNo.getId());
		phoneNo.setName(namePhoneNo.getName());
		phoneNo.setOffice(namePhoneNo.isOffice());
		phoneNo.setPhoneNo(namePhoneNo.getPhoneNo());

		return phoneNo;
	}

	@Override
	public RespStoreType mapStoreType(StoreType storeType) {

		if (storeType != null) {
			RespStoreType respStoreType = new RespStoreType();
			respStoreType.setDescription(storeType.getDescription());
			respStoreType.setId(storeType.getId());
			respStoreType.setName(storeType.getName());
			respStoreType.setValue(storeType.getValue());
			return respStoreType;
		}

		return null;
	}

	@Override
	public Store mapStore(StoreReq storeReq) {

		if (storeReq != null) {

			Store store = new Store();

			store.setDescription(storeReq.getDescription());
			store.setEmail(storeReq.getEmail());
			store.setName(storeReq.getName());
			store.setProprietor(storeReq.getProprietor());

			return store;
		}

		return null;
	}

	@Override
	public RespStore mapRespStoreOnly(Store store) {
		if (store != null) {
			RespStore respStore = new RespStore();

//			respStore.setStartLine(store.getStartLine());
//
//			respStore.setDescription(store.getDescription());
//
//			respStore.setEmail(store.getEmail());
//			respStore.setId(store.getPublicId());
//			respStore.setName(store.getName());
//			respStore.setProprietor(store.getProprietor());
//			respStore.setWebsite(store.getWebsite());
//			respStore.setLogoUrl(store.getLogoUrl());
//
//			if (store.getStoreType() != null) {
//				RespStore storeType = mapStoreType(store.getStoreType());
//				respStore.setStorType(storeType);
//			}

			return respStore;
		}

		return null;

	}

	@Override
	public List<RespStore> mapAllRespStore(List<Store> stores) {

		if (stores != null) {
			List<RespStore> list = new ArrayList<>();

			for (Store store : stores) {
				RespStore respStore = mapRespStore(store);

				if (respStore != null) {
					list.add(respStore);
				}
			}

			return list;
		}
		return null;
	}

	@Override
	public NamePhoneNo mapNameAndPhoneNo(NamePhoneNoReq namePhoneNo) {

		if (namePhoneNo != null) {

			NamePhoneNo phoneNo = new NamePhoneNo();
			phoneNo.setName(namePhoneNo.getName());
			phoneNo.setPhoneNo(namePhoneNo.getPhoneNo());
			phoneNo.setOffice(namePhoneNo.isOffice());

			return phoneNo;
		}

		return null;
	}

	@Override
	public List<RespStoreType> mapAllRespStoreType(List<StoreType> storeTypes) {

		if (storeTypes != null) {
			List<RespStoreType> respStoreTypes = new ArrayList<>();

			for (StoreType storeType : storeTypes) {

				RespStoreType respStoreType = mapRespStoreType(storeType);

				if (respStoreType != null) {
					respStoreTypes.add(respStoreType);
				}

			}

			return respStoreTypes;
		}
		return null;
	}

	@Override
	public RespStoreType mapRespStoreType(StoreType storeType) {

		RespStoreType respStoreType = mapRespStoreType(storeType);

		if (respStoreType != null) {
			return respStoreType;
		}
		return null;
	}

	@Override
	public RespStoreType mapRespStoreTypeOnly(StoreType storeType) {

		if (storeType != null) {

			RespStoreType type = new RespStoreType();
			type.setDescription(storeType.getDescription());
			type.setName(storeType.getName());
			type.setId(storeType.getId());
			type.setValue(storeType.getValue());
			return type;
		}

		return null;
	}

	@Override
	public List<RespInvoiceSetting> mapAllInvoiceSetting(List<InvoiceSetting> invoiceSettings) {

		if (invoiceSettings != null) {
			List<RespInvoiceSetting> settings = new ArrayList<>();

			for (InvoiceSetting invoiceSetting : invoiceSettings) {
//				RespInvoiceSetting setting = mapInvoiceSettingOnly(invoiceSetting);

//				if (setting != null) {
//					settings.add(setting);
//				}
			}

			return settings;
		}
		return null;
	}

	@Override
	public RespInvoiceSetting getSettingByValue(String value, List<InvoiceSetting> invoiceSettings) {

		if (invoiceSettings != null) {

			RespInvoiceSetting setting = null;

			for (InvoiceSetting invoiceSetting : invoiceSettings) {

				if (invoiceSetting != null) {

				}
			}

			return setting;
		}
		return null;
	}

	

}
