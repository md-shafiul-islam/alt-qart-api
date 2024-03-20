package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.client.model.PathaoWebHook;
import com.altqart.client.req.model.PathaoWebHookReq;
import com.altqart.mapper.ParcelMapper;
import com.altqart.mapper.ParcelProviderMapper;
import com.altqart.model.Parcel;
import com.altqart.model.ParcelStatus;
import com.altqart.resp.model.RespParcel;
import com.altqart.resp.model.RespParcelStatus;

@Service
public class ParcelMapperImpl implements ParcelMapper {

	@Autowired
	private ParcelProviderMapper parcelProviderMapper;

	@Override
	public List<RespParcel> mapAllParcel(List<Parcel> parcelList) {

		if (parcelList != null) {

			List<RespParcel> parcels = new ArrayList<>();

			for (Parcel parcel : parcelList) {

				RespParcel respParcel = mapRespParcelOnly(parcel);

				if (respParcel != null) {
					parcels.add(respParcel);
				}

			}

			return parcels;

		}
		return null;
	}

	public RespParcel mapRespParcelOnly(Parcel parcel) {

		if (parcel != null) {
			RespParcel respParcel = new RespParcel();
			
			respParcel.setAmountToCollect(parcel.getAmountToCollect());
			respParcel.setConsignmentId(parcel.getConsignmentId());
			respParcel.setDate(parcel.getDate());
			respParcel.setDateGroup(parcel.getDateGroup());
			respParcel.setDeliveryFee(parcel.getDeliveryFee());

			if (parcel.getDeliveryType() == 12) {
				respParcel.setDeliveryType("On Demand Delivery");
			} else {
				respParcel.setDeliveryType("Normal Delivery");
			}

			respParcel.setId(parcel.getPublicId());
			respParcel.setItemDescription(parcel.getItemDescription());
			respParcel.setItemQuantity(parcel.getItemQuantity());

			if (parcel.getItemType() == 1) {
				respParcel.setItemType("Document");
			} else {
				respParcel.setItemType("Parcel");
			}

			respParcel.setItemWeight(parcel.getItemWeight());
			respParcel.setMerchantOrderId(parcel.getMerchantOrderId());
			respParcel.setParcelProvider(parcelProviderMapper.mapRespMinParcelProviderOnly(parcel.getParcelProvider()));
			respParcel.setParcelStatus(mapParcelStatusOnly(parcel.getParcelStatus()));
			respParcel.setRecipientAddress(parcel.getRecipientAddress());
			
			
			
			respParcel.setRecipientArea(parcel.getRecipientArea());
			respParcel.setRecipientCity(parcel.getRecipientCity());
			respParcel.setRecipientName(parcel.getRecipientName());
			respParcel.setRecipientPhone(parcel.getRecipientPhone());
			respParcel.setRecipientZone(parcel.getRecipientZone());

			respParcel.setSenderName(parcel.getSenderName());
			respParcel.setSenderPhone(parcel.getSenderPhone());
			respParcel.setSpecialInstruction(parcel.getSpecialInstruction());
			respParcel.setStatus(parcel.getStatus());
			respParcel.setStoreId(parcel.getStoreId());
			
			respParcel.setParcelStatus(mapParcelStatusOnly(parcel.getParcelStatus()));
			
			return respParcel;
		}
		return null;
	}

	@Override
	public Parcel mapTempParcel(Parcel parcel) {
		
		Parcel tempParcel = new Parcel();
		tempParcel.setAmountToCollect(parcel.getAmountToCollect());
		tempParcel.setConsignmentId(parcel.getConsignmentId());
		tempParcel.setDate(parcel.getDate());
		tempParcel.setDateGroup(parcel.getDateGroup());
		tempParcel.setDeliveryFee(parcel.getDeliveryFee());

		tempParcel.setDeliveryType(parcel.getDeliveryType());
		
		tempParcel.setItemDescription(parcel.getItemDescription());
		tempParcel.setItemQuantity(parcel.getItemQuantity());
		tempParcel.setItemType(parcel.getItemType());

		tempParcel.setItemWeight(parcel.getItemWeight());
		tempParcel.setMerchantOrderId(parcel.getMerchantOrderId());
		
		tempParcel.setRecipientAddress(parcel.getRecipientAddress());

		tempParcel.setRecipientArea(parcel.getRecipientArea());
		tempParcel.setRecipientCity(parcel.getRecipientCity());
		tempParcel.setRecipientName(parcel.getRecipientName());
		tempParcel.setRecipientPhone(parcel.getRecipientPhone());
		tempParcel.setRecipientZone(parcel.getRecipientZone());

		tempParcel.setSenderName(parcel.getSenderName());
		tempParcel.setSenderPhone(parcel.getSenderPhone());
		tempParcel.setSpecialInstruction(parcel.getSpecialInstruction());
		tempParcel.setStatus(parcel.getStatus());
		tempParcel.setStoreId(parcel.getStoreId());
		
		return tempParcel;
		
	}
	
	public RespParcelStatus mapParcelStatus(ParcelStatus parcelStatus) {

		RespParcelStatus respParcelStatus = mapParcelStatusOnly(parcelStatus);
		if (respParcelStatus != null) {
			respParcelStatus.setParcels(mapAllParcel(parcelStatus.getParcels()));
		}

		return respParcelStatus;
	}

	public RespParcelStatus mapParcelStatusOnly(ParcelStatus parcelStatus) {

		if (parcelStatus != null) {

			RespParcelStatus respParcelStatus = new RespParcelStatus();
			respParcelStatus.setId(parcelStatus.getId());
			respParcelStatus.setName(parcelStatus.getLabel());
			respParcelStatus.setValue(parcelStatus.getSlug());

			return respParcelStatus;
		}

		return null;
	}
	
	@Override
	public PathaoWebHook mapPathaoWebHook(PathaoWebHookReq webHookReq) {
		// TODO Auto-generated method stub
		return null;
	}
}
