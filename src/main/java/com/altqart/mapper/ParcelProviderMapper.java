package com.altqart.mapper;

import java.util.List;

import com.altqart.model.ParcelProvider;
import com.altqart.req.model.ParcelProviderReq;
import com.altqart.resp.model.RespMinParcelProvider;
import com.altqart.resp.model.RespParcelProvider;

public interface ParcelProviderMapper {

	public ParcelProvider mapParcelProvider(ParcelProviderReq parcelProviderReq);

	public RespParcelProvider mapRespParcelProvider(ParcelProvider provider);

	public RespParcelProvider mapRespParcelProviderOnly(ParcelProvider parcelProvider);

	public List<ParcelProvider> mapAllRespParcelProvider(List<ParcelProvider> providers);

	public RespMinParcelProvider mapRespMinParcelProviderOnly(ParcelProvider parcelProvider);

}
