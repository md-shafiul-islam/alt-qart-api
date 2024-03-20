package com.altqart.services;

import java.util.Map;

import com.altqart.client.req.model.PathaoWebHookReq;
import com.altqart.model.Parcel;
import com.altqart.req.model.ParcelReq;

public interface ParcelServices {

	public void getAllParcel(Map<String, Object> map, int start, int size, String type);

	public void addParcel(ParcelReq parcelReq, Map<String, Object> map);

	public void updateParcel(ParcelReq parcelReq, Map<String, Object> map);

	public void getParcelById(String id);

	public void createPathaoParcel(Parcel parcel, Map<String, Object> map);

	public void sendParcel(String id, Map<String, Object> map);

	public Map<String, Object> updatePathaoParcelViaWebHook(PathaoWebHookReq webHookReq, String givenKey);

	public void getAllPendingParcel(Map<String, Object> map, int start, int size);

	public void getAllSendParcel(Map<String, Object> map, int start, int size);

	public void getAllDeliveredParcel(Map<String, Object> map, int start, int size);

	public void getAllDeliveryFailedParcel(Map<String, Object> map, int start, int size);

	public void getAllPaymentInvoiceParcel(Map<String, Object> map, int start, int size);

	public void getAllReturnParcel(Map<String, Object> map, int start, int size);

}
