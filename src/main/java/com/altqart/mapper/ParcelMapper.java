package com.altqart.mapper;

import java.util.List;

import com.altqart.client.model.PathaoWebHook;
import com.altqart.client.req.model.PathaoWebHookReq;
import com.altqart.model.Parcel;
import com.altqart.resp.model.RespParcel;

public interface ParcelMapper {

	public List<RespParcel> mapAllParcel(List<Parcel> parcelList);
	
	public RespParcel mapRespParcelOnly(Parcel parcel);

	public Parcel mapTempParcel(Parcel dbParcel);

	public PathaoWebHook mapPathaoWebHook(PathaoWebHookReq webHookReq);

}
